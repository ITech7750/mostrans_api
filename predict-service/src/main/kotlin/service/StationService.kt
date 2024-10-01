package ru.itech.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.itech.dto.StationDTO

@Service
interface StationService {
    fun getAllStationsPaginate(pageable: Pageable): List<StationDTO>

    fun getAllStations(): List<StationDTO>

    fun getStationById(id: Long): StationDTO

    fun getStationsByDateTime(datetime: String): List<StationDTO>

    fun createStation(stationDTO: StationDTO): StationDTO

    fun updateStation(
        id: Long,
        stationDTO: StationDTO,
    ): StationDTO

    fun deleteStation(id: Long)

    fun predictPassengerFlow(
        line: String,
        name: String,
        squareMeters: Double?,
        buildingType: String?,
        datetime: String
    ): List<StationDTO>
}
