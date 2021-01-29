package ru.adsc.weatherdata.data.model

class dLocation {
    private var longitude = 0f
    private var latitude = 0f
    private var sunset: Long = 0
    private var sunrise: Long = 0
    private var country: String? = null
    private var city: String? = null

    fun getLongitude(): Float {
        return longitude
    }

    fun setLongitude(longitude: Float) {
        this.longitude = longitude
    }

    fun getLatitude(): Float {
        return latitude
    }

    fun setLatitude(latitude: Float) {
        this.latitude = latitude
    }

    fun getSunset(): Long {
        return sunset
    }

    fun setSunset(sunset: Long) {
        this.sunset = sunset
    }

    fun getSunrise(): Long {
        return sunrise
    }

    fun setSunrise(sunrise: Long) {
        this.sunrise = sunrise
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getCity(): String? {
        return city
    }

    fun setCity(city: String?) {
        this.city = city
    }
}