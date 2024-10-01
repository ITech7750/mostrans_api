package ru.itech.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itech.entity.Station

@Repository
interface StationRepository : JpaRepository<Station, Long> {
    fun findByNameAndLine(
        name: String,
        line: String,
    ): Station?

    @Query("SELECT s FROM Station s")
    fun findByOrderById(pageable: Pageable): List<Station>


}
