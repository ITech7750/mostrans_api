package ru.itech.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itech.entity.StationConnection


@Repository
interface StationConnectionRepository : JpaRepository<StationConnection, Long> {
}
