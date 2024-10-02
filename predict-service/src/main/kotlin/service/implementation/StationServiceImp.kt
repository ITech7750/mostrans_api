package ru.itech.service.implementation

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itech.dto.StationDTO
import ru.itech.dto.StationFrontendDTO
import ru.itech.dto.toEntity
import ru.itech.entity.StationGraph
import ru.itech.entity.StationPredictor
import ru.itech.repository.StationConnectionRepository
import ru.itech.repository.StationPassengerFlowRepository
import ru.itech.repository.StationRepository
import ru.itech.service.StationService

@Service
@Transactional
open class StationServiceImpl(
    private val stationRepository: StationRepository,
    private val stationPassengerFlowRepository: StationPassengerFlowRepository,
    private val stationConnectionRepository: StationConnectionRepository,
) : StationService {

    // Получение всех станций с последними потоками пассажиров
    override fun getAllStations(): List<StationDTO> {
        val stations = stationRepository.findAll()

        return stations.map { station ->
            val latestPassengerFlow = stationPassengerFlowRepository.findTopByStationOrderByDatetimeDesc(station)?.passengerFlow
            station.toDTO(latestPassengerFlow)
        }
    }

    // Получение всех станций с пагинацией
    override fun getAllStationsPaginate(pageable: Pageable): List<StationDTO> {
        val stations = stationRepository.findByOrderById(pageable)
        return stations.map { station ->
            val latestPassengerFlow = stationPassengerFlowRepository.findTopByStationOrderByDatetimeDesc(station)?.passengerFlow
            station.toDTO(latestPassengerFlow)
        }
    }

    // Получение станции по ID с актуальным потоком пассажиров
    override fun getStationById(id: Long): StationDTO {
        val station = stationRepository.findById(id).orElseThrow {
            Exception("Station not found with id: $id")
        }
        val latestPassengerFlow = stationPassengerFlowRepository.findTopByStationOrderByDatetimeDesc(station)?.passengerFlow
        return station.toDTO(latestPassengerFlow)
    }

    fun buildStationGraph(stations: List<StationDTO>): StationGraph {
        val stationGraph = StationGraph()

        // Добавляем станции в граф
        stations.forEach { stationDTO ->
            val stationEntity = stationDTO.toEntity()
            val passengerFlows = stationPassengerFlowRepository.findAllByStation(stationEntity)

            if (passengerFlows.isNotEmpty()) {
                stationEntity.passengerFlows = passengerFlows.toMutableList()
            }

            stationGraph.addStation(stationEntity)
        }

        // Получаем все соединения между станциями из базы данных
        val stationConnections = stationConnectionRepository.findAll()
        println("Соединения станций: $stationConnections")

        // Связываем станции в графе на основе данных из таблицы соединений
        for (i in 0 until stationConnections.size-1) {
            val connection = stationConnections[i]
            println(connection)
            stationGraph.connectStations(connection.station1Id, connection.station2Id)
        }

        return stationGraph
    }

    fun calculateAdditionalLoad(squareMeters: Double, buildingType: String): Int {
        var people = squareMeters / when (buildingType) {
            "office" -> 35
            "офисное" -> 35
            "residential" -> 25
            "жилое" -> 25
            else -> throw Exception("Unknown building type '$buildingType'")
        }

        //трудоспособное население
        people *= 0.57;
        //те, кто используют ОТ
        people *= 0.7;
        return people.toInt()
    }

    override fun predictPassengerFlowForFrontend(
        line: String,
        name: String,
        squareMeters: Double?,
        buildingType: String?,
        datetime: String
    ): Map<String, Map<String, Double>> {
        // Предсказание пассажиропотока с использованием существующего метода predict
        val stations = predict(line, name, squareMeters, buildingType, datetime)
        println(stations)
        // Преобразование данных в Map<String, Double> (где ключ - имя станции, а значение - passengerFlow)
        val stationFlows: Map<String, Double> = stations.associate { stationDTO ->
            stationDTO.name to (stationDTO.passengerFlow?.toDouble()?.div(100000) ?: 10000.0)
        }
        println(stationFlows)
        // Возвращаем объект в формате Map<String, Map<String, Double>>
        return mapOf("stations" to stationFlows)
    }





    // Остальные методы остаются без изменений...
    override fun predictPassengerFlow(
        line: String,
        name: String,
        squareMeters: Double?,
        buildingType: String?,
        datetime: String
    ): List<StationDTO> {
        return predict(line, name, squareMeters, buildingType, datetime)
    }

    fun predict(
        line: String,
        name: String,
        squareMeters: Double?,
        buildingType: String?,
        datetime: String
    ): List<StationDTO> {
        // Получаем все станции и их актуальный поток на момент времени datetime
        val stations = stationRepository.findAll().map { station ->
            val passengerFlowAtDatetime = station.getFlowByDatetime(datetime) ?: 0
            station.toDTO(passengerFlowAtDatetime)
        }

        // Строим граф станций
        val stationGraph = buildStationGraph(stations)
        //val prevFlow = stationGraph.getAllStations().map { station -> station.passengerLoad }

        // Поиск индекса станции по имени и линии
        val startStationIndex = stationGraph.getAllStations().indexOfFirst { it.name == name && it.line == line }
        if (startStationIndex == -1) {
            throw Exception("Station with name '$name' on line '$line' not found")
        }

        // Предсказание пассажиропотока
        val predictor = StationPredictor()
        predictor.predictForStation(
            stationGraph = stationGraph,
            startStationIndex = startStationIndex,
            additionalLoad = calculateAdditionalLoad(squareMeters ?: 0.0, buildingType ?: "residential")
        )

        // Возвращаем обновленные данные о станциях с предсказанными потоками
        return stationGraph.getAllStations().map { station -> station.toDTO(station.passengerLoad) }
    }
    // Создание новой станции
    override fun createStation(stationDTO: StationDTO): StationDTO {
        // Преобразуем DTO в сущность
        val station = stationDTO.toEntity()

        // Сначала сохраняем сущность станции, чтобы она получила ID
        val savedStation = stationRepository.save(station)

        // Если есть пассажиропотоки, сохраняем их и связываем с сохраненной станцией
        station.passengerFlows.forEach { passengerFlow ->
            passengerFlow.station = savedStation  // Устанавливаем ссылку на сохраненную станцию
            stationPassengerFlowRepository.save(passengerFlow)  // Сохраняем пассажиропотоки
        }

        // Находим последний пассажиропоток (если он есть) для этой станции
        val latestPassengerFlow = stationPassengerFlowRepository.findTopByStationOrderByDatetimeDesc(savedStation)?.passengerFlow

        // Возвращаем DTO сохраненной станции с последним пассажиропотоком
        return savedStation.toDTO(latestPassengerFlow)
    }


    // Обновление станции по ID
    override fun updateStation(id: Long, stationDTO: StationDTO): StationDTO {
        val existingStation = stationRepository.findById(id).orElseThrow {
            Exception("Station not found with id: $id")
        }
        val updatedStation = existingStation.copy(
            name = stationDTO.name,
            line = stationDTO.line
        )
        val latestPassengerFlow = stationPassengerFlowRepository.findTopByStationOrderByDatetimeDesc(updatedStation)?.passengerFlow
        return stationRepository.save(updatedStation).toDTO(latestPassengerFlow)
    }

    // Удаление станции по ID
    override fun deleteStation(id: Long) {
        stationRepository.deleteById(id)
    }
}


