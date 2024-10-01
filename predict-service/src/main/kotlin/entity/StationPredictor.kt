package ru.itech.entity

import predict.*

class StationPredictor : Predictor() {

    // Пример использования предсказания для конкретной станции
    fun predictForStation(stationGraph: StationGraph, startStationIndex: Int, additionalLoad: Int, centerRadius: Int) {
        setGraph(stationGraph)
        setStartNodeIndex(startStationIndex)
        setAdditionalLoad(additionalLoad)
        setCenterRadius(centerRadius)
        calculateLoad()

        // Вывести результат предсказания для всех станций
        stationGraph.getAllStations().forEach { station ->
            println("Станция: ${station.name}, Пассажиропоток: ${station.passengerLoad}")
        }


    }
}
