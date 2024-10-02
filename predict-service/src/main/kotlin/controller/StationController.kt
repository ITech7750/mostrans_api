package ru.itech.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itech.dto.PassengerFlowRequest
import ru.itech.dto.StationDTO
import ru.itech.dto.StationFrontendDTO
import ru.itech.service.StationService

@RestController
@RequestMapping("/api/stations")
@Tag(name = "Station Controller", description = "Управление станциями метрополитена") // Swagger Tag
class StationController(
    private val stationService: StationService,
) {

    // 1. Создать новую станцию
    @Operation(summary = "Создать новую станцию", description = "Создаёт станцию с указанными параметрами")
    @PostMapping("/create")
    fun createStation(
        @RequestBody stationDTO: StationDTO,
    ): ResponseEntity<StationDTO> {
        val createdStation = stationService.createStation(stationDTO)
        return ResponseEntity.ok(createdStation)
    }

    // 2. Получить все станции с постраничным выводом
    @Operation(summary = "Получить все станции", description = "Возвращает список станций с поддержкой постраничного вывода")
    @GetMapping
    fun getAllStations(
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<List<StationDTO>> {
        val stations = stationService.getAllStationsPaginate(pageable)
        return ResponseEntity.ok(stations)
    }

    // 3. Обновить станцию по id
    @Operation(summary = "Обновить станцию", description = "Обновляет станцию по её идентификатору")
    @PutMapping("/{id}")
    fun updateStation(
        @Parameter(description = "ID станции") @PathVariable id: Long,
        @RequestBody stationDTO: StationDTO,
    ): ResponseEntity<StationDTO> {
        val updatedStation = stationService.updateStation(id, stationDTO)
        return ResponseEntity.ok(updatedStation)
    }

    // 4. Удалить станцию по id
    @Operation(summary = "Удалить станцию", description = "Удаляет станцию по её идентификатору")
    @DeleteMapping("/{id}")
    fun deleteStation(
        @Parameter(description = "ID станции") @PathVariable id: Long,
    ): ResponseEntity<Void> {
        stationService.deleteStation(id)
        return ResponseEntity.noContent().build()
    }

    // 5. Прогнозирование пассажиропотока (StationDTO)
    @PostMapping("/predict")
    @Operation(summary = "Прогнозирование пассажиропотока", description = "Прогнозирование пассажиропотока с полными данными станции (StationDTO)")
    fun predictPassengerFlow(
        @Parameter(description = "Данные для прогноза", required = true)
        @RequestBody request: PassengerFlowRequest
    ): ResponseEntity<List<StationDTO>> {
        val stations = stationService.predictPassengerFlow(
            line = request.line,
            name = request.name,
            squareMeters = request.squareMeters,
            buildingType = request.buildingType,
            datetime = request.datetime
        )
        return ResponseEntity.ok(stations)
    }

    // 6. Прогнозирование пассажиропотока для фронтенда (StationFrontendDTO)
    @PostMapping("/predict/frontend")
    @Operation(summary = "Прогнозирование пассажиропотока для фронтенда", description = "Прогнозирование пассажиропотока с минимальной информацией для фронтенда")
    fun predictPassengerFlowForFrontend(
        @Parameter(description = "Данные для прогноза", required = true)
        @RequestBody request: PassengerFlowRequest
    ): ResponseEntity<Map<String, Map<String, Double>>> {
        val stations = stationService.predictPassengerFlowForFrontend(
            line = request.line,
            name = request.name,
            squareMeters = request.squareMeters,
            buildingType = request.buildingType,
            datetime = request.datetime
        )
        return ResponseEntity.ok(stations)
    }
}
