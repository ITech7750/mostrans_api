
package ru.itech.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itech.entity.Station
import ru.itech.entity.StationPassengerFlow

@Repository
interface StationPassengerFlowRepository : JpaRepository<StationPassengerFlow, Long> {

    @Query("SELECT spf FROM StationPassengerFlow spf WHERE spf.station = :station")
    fun findAllByStation(station: Station): List<StationPassengerFlow>

    // Поиск последнего пассажиропотока для станции
    fun findTopByStationOrderByDatetimeDesc(station: Station): StationPassengerFlow?

    // Существующий метод для поиска по станции и дате
    fun findByStationAndDatetime(station: Station, datetime: String): StationPassengerFlow?

}
