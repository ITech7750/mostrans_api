package ru.itech.entity

import jakarta.persistence.*
import predict.Graph
import ru.itech.dto.StationDTO

@Entity
@Table(name = "stations")
data class Station(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val line: String,

    @Column(nullable = true)
    private var distanceFromCenter: Double = 0.0,  // Расстояние до центра

    @OneToMany(mappedBy = "station", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var passengerFlows: List<StationPassengerFlow> = mutableListOf()

) : Graph.Node() {

    override fun getPassengerLoad(): Int {
        // Возвращаем текущий пассажиропоток (или значение по умолчанию)
        return passengerFlows.maxByOrNull { it.datetime }?.passengerFlow ?: 0
    }

    override fun setPassengerLoad(passengerLoad: Int) {
        // Можно добавить логику для обновления passengerLoad
        // Но здесь просто пример с заглушкой
    }

    override fun getDistanceToCenter(): Int {
        return distanceFromCenter.toInt()
    }

    override fun setDistanceToCenter(distanceToCenter: Int) {
        this.distanceFromCenter = distanceToCenter.toDouble()
    }

    fun getFlowByDatetime(datetime: String): Int? {
        return passengerFlows.find { it.datetime == datetime }?.passengerFlow
    }

    fun addConnection(station: Station) {
        this.getNext().add(station)
    }

    fun toDTO(latestPassengerFlow: Int?): StationDTO {
        return StationDTO(
            id = this.id,
            name = this.name,
            line = this.line,
            passengerFlow = latestPassengerFlow
        )
    }
}
