package ru.adsc.weatherdata.data.processing

import org.json.JSONException
import org.json.JSONObject
import ru.adsc.weatherdata.data.model.dDayForecast
import ru.adsc.weatherdata.data.model.dLocation
import ru.adsc.weatherdata.data.model.dWeather
import ru.adsc.weatherdata.data.model.dWeatherForecast


class JSONWeatherParser {
    companion object    {
        val parent = JSONWeatherParser()

        @Throws(JSONException::class)
        fun getWeather(data: String?): dWeather? {
            val weather = dWeather()

            // We create out JSONObject from the data
            val jObj = JSONObject(data)

            // We start extracting the info
            val loc = dLocation()
            val coordObj = parent.getObject("coord", jObj)
            loc.setLatitude(parent.getFloat("lat", coordObj))
            loc.setLongitude(parent.getFloat("lon", coordObj))
            val sysObj = parent.getObject("sys", jObj)
            loc.setCountry(parent.getString("country", sysObj))
            loc.setSunrise(parent.getLong("sunrise", sysObj))
            loc.setSunset(parent.getLong("sunset", sysObj))
            loc.setCity(parent.getString("name", jObj))
            weather.location = loc

            // We get weather info (This is an array)
            val jArr = jObj.getJSONArray("weather")

            // We use only the first value
            val JSONWeather = jArr.getJSONObject(0)
            //weather.currentCondition.setWeatherId(getInt("id", JSONWeather))
            weather.currentCondition.weatherId = parent.getInt("id", JSONWeather)
            weather.currentCondition.descr = parent.getString("description", JSONWeather)
            weather.currentCondition.condition = parent.getString("main", JSONWeather)
            weather.currentCondition.icon = parent.getString("icon", JSONWeather)
            val mainObj = parent.getObject("main", jObj)
            weather.currentCondition.humidity = parent.getFloat("humidity", mainObj)
            weather.currentCondition.pressure = parent.getFloat("pressure", mainObj)
            weather.temperature.maxTemp = parent.getFloat("temp_max", mainObj)
            weather.temperature.minTemp = parent.getFloat("temp_min", mainObj)
            weather.temperature.temp = parent.getFloat("temp", mainObj)

            // Wind
            val wObj = parent.getObject("wind", jObj)
            weather.wind.speed = parent.getFloat("speed", wObj)
            weather.wind.deg = parent.getFloat("deg", wObj)

            // Clouds
            val cObj = parent.getObject("clouds", jObj)
            weather.clouds.perc = parent.getInt("all", cObj)

            // We download the icon to show
            return weather
        }

        @Throws(JSONException::class)
        fun getForecastWeather(data: String?): dWeatherForecast? {
            val forecast = dWeatherForecast()

            // We create out JSONObject from the data
            val jObj = JSONObject(data)
            val jArr = jObj.getJSONArray("list") // Here we have the forecast for every day
            forecast.numRecords = jArr.length()
            // We traverse all the array and parse the data
            for (i in 0 until jArr.length()) {
                val jDayForecast = jArr.getJSONObject(i)

                // Now we have the json object so we can extract the data
                val df = dDayForecast()

                // We retrieve the timestamp (dt)
                df.timestamp = jDayForecast.getLong("dt")

                // Temp is an object
                val jTempObj = jDayForecast.getJSONObject("temp")
                df.forecastTemp.day = jTempObj.getDouble("day").toFloat()
                df.forecastTemp.min = jTempObj.getDouble("min").toFloat()
                df.forecastTemp.max = jTempObj.getDouble("max").toFloat()
                df.forecastTemp.night = jTempObj.getDouble("night").toFloat()
                df.forecastTemp.eve = jTempObj.getDouble("eve").toFloat()
                df.forecastTemp.morning = jTempObj.getDouble("morn").toFloat()

                // Pressure & Humidity
                df.weather.currentCondition.pressure = jDayForecast.getDouble("pressure").toFloat()
                df.weather.currentCondition.humidity = jDayForecast.getDouble("humidity").toFloat()

                // ...and now the weather
                val jWeatherArr = jDayForecast.getJSONArray("weather")
                val jWeatherObj = jWeatherArr.getJSONObject(0)
                df.weather.currentCondition.weatherId = parent.getInt("id", jWeatherObj)
                df.weather.currentCondition.descr = parent.getString("description", jWeatherObj)
                df.weather.currentCondition.condition = parent.getString("main", jWeatherObj)
                df.weather.currentCondition.icon = parent.getString("icon", jWeatherObj)
                forecast.addForecast(df)
            }
            return forecast
        }
    }

    @Throws(JSONException::class)
    private fun getObject(tagName: String, jObj: JSONObject): JSONObject {
        return jObj.getJSONObject(tagName)
    }

    @Throws(JSONException::class)
    private fun getString(tagName: String, jObj: JSONObject): String? {
        return jObj.getString(tagName)
    }

    @Throws(JSONException::class)
    private fun getFloat(tagName: String, jObj: JSONObject): Float {
        return jObj.getDouble(tagName).toFloat()
    }

    @Throws(JSONException::class)
    private fun getInt(tagName: String, jObj: JSONObject): Int {
        return jObj.getInt(tagName)
    }

    @Throws(JSONException::class)
    private fun getLong(tagName: String, jObj: JSONObject): Long {
        return jObj.getLong(tagName)
    }
}