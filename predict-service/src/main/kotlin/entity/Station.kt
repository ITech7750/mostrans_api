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
    var distanceFromCenter: Int = 0,  // Расстояние до центра

    @OneToMany(mappedBy = "station", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var passengerFlows: List<StationPassengerFlow> = mutableListOf()

     // Список пассажиропотоков для разных временных промежутков
) : Graph.Node(0, distanceFromCenter) {


    fun getFlowByDatetime(datetime: String): Int? {
        return passengerFlows.find { it.datetime == datetime }?.passengerFlow
    }

    fun addConnection(station: Station) {
        this.getNext().add(station)
    }

}
public fun Station.toDTO(latestPassengerFlow: Int?): StationDTO {
    return StationDTO(
        id = this.id,
        name = this.name,
        line = this.line,
        passengerFlow = latestPassengerFlow  // Передаем актуальный пассажиропоток
    )
}

