package ru.itech.entity

import jakarta.persistence.*

@Entity
@Table(name = "station_passenger_flow")
data class StationPassengerFlow(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    var station: Station,

    @Column(nullable = false)
    val datetime: String,

    @Column(nullable = false)
    var passengerFlow: Int
)
