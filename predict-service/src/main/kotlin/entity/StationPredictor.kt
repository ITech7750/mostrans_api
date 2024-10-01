package ru.itech.entity

import predict.*

class StationPredictor : Predictor() {
    companion object {
        private const val CENTER_RADIUS = 150;
    }
    // Пример использования предсказания для конкретной станции
    fun predictForStation(stationGraph: StationGraph, startStationIndex: Int, additionalLoad: Int) {
        setGraph(stationGraph)
        setStartNodeIndex(startStationIndex)
        setAdditionalLoad(additionalLoad)
        setCenterRadius(CENTER_RADIUS)
        calculateLoad()

        // Вывести результат предсказания для всех станций
        stationGraph.getAllStations().forEach { station ->
            println("Станция: ${station.name}, Пассажиропоток: ${station.passengerLoad}")
        }


    }
}
