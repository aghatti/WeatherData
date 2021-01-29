package ru.adsc.weatherdata.data.model

import java.text.SimpleDateFormat
import java.util.*

class dDayForecast {
    private val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    var weather: dWeather = dWeather()
    var forecastTemp = ForecastTemp()
    var timestamp: Long = 0

    class ForecastTemp {
        var day = 0f
        var min = 0f
        var max = 0f
        var night = 0f
        var eve = 0f
        var morning = 0f
    }

    fun getStringDate(): String? {
        return sdf.format(Date(timestamp * 1000))
    }
}