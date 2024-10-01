package ru.itech.dto

import ru.itech.entity.Station
import ru.itech.entity.StationPassengerFlow

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

fun StationDTO.toTemp(): Temp {
    return Temp(
        name = this.name,
        value = this.passengerFlow
    )
}