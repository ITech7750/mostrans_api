package ru.itech.entity

import predict.Graph

class StationGraph : Graph() {

    // Добавление станции в граф
    fun addStation(station: Station): Int {
        return addNode(station)
    }

    // Связывание двух станций
    fun connectStations(a: Long, b: Long) {
        addEdge(a.toInt(), b.toInt())
    }

    // Получение станции по индексу
    fun getStation(index: Long): Station? {
        return getNode(index.toInt()) as? Station
    }

    // Пример метода для получения всех станций в графе
    fun getAllStations(): List<Station> {
        return this.map { it as Station }
    }

    // Проверка, соединены ли две станции
    fun areStationsConnected(a: Long, b: Long): Boolean {
        return hasEdge(a.toInt(), b.toInt())
    }
}
