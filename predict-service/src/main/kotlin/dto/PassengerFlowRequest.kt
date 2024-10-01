package ru.itech.dto

data class PassengerFlowRequest(
    val line: String,
    val name: String,
    val squareMeters: Double?,
    val buildingType: String?,
    val datetime: String
)
