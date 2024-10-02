package ru.itech.dto

import ru.itech.entity.Station

data class StationDTO(
    val id: Long? = null,
    val name: String,
    val line: String,
    val passengerFlow: Int? = null  // Актуальный пассажиропоток
)


fun StationDTO.toEntity(): Station {
    return Station(
        id = this.id ?: 0,
        name = this.name,
        line = this.line
    )
}

