package ru.adsc.weatherdata.data.model

class dWeatherForecast {
    private val daysForecast: MutableList<dDayForecast> = mutableListOf()
    var numRecords:Int = 0

    fun addForecast(forecast: dDayForecast) {
        daysForecast.add(forecast)
    }

    fun getForecast(dayNum: Int): dDayForecast? {
        return daysForecast[dayNum]
    }
}