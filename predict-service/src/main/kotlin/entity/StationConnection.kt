package ru.itech.entity

import jakarta.persistence.*

@Entity
@Table(name = "station_connections")
data class StationConnection(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "station1_id", nullable = false)
    val station1Id: Long,

    @Column(name = "station2_id", nullable = false)
    val station2Id: Long
)

