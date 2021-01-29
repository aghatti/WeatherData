package ru.adsc.weatherdata.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.adsc.weatherdata.data.entities.City

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY city_name ASC")
    fun getAlphabetizedCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City): Long

    @Query("DELETE FROM cities")
    suspend fun deleteAll()

}