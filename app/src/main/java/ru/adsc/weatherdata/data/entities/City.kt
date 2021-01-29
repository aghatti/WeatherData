package ru.adsc.weatherdata.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="cities")
data class City(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    //@ColumnInfo(name = "city_name") val cityName: String
    @PrimaryKey @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "country_code") val countryCode: String

//,
    //
)