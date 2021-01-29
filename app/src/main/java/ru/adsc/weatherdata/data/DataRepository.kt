package ru.adsc.weatherdata.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import ru.adsc.weatherdata.data.entities.City
import ru.adsc.weatherdata.data.model.dDayForecast
import ru.adsc.weatherdata.data.model.dWeatherForecast
import ru.adsc.weatherdata.data.processing.JSONWeatherParser
import ru.adsc.weatherdata.mApp

class DataRepository(private val cityDao: CityDao) {
    val allCities: Flow<List<City>> = cityDao.getAlphabetizedCities()
    val weatherRecords: MutableLiveData<List<dDayForecast>> = MutableLiveData()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city: City): Long {
        val res = cityDao.insert(city)
        return res
    }
    fun clearWeatherForecast()  {
        weatherRecords.value =  mutableListOf()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getWeatherForecast(reqUrl: String) {
        val socketTimeout = 30000
        val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val requestQueue: RequestQueue = Volley.newRequestQueue(mApp.instance)

        var lwRecords:MutableList<dDayForecast> = mutableListOf()
        val stringRequest = StringRequest(Request.Method.GET,
                reqUrl,
                { response ->
                    val jsob = JSONObject(response)
                    val weatherForecast: dWeatherForecast? = JSONWeatherParser.getForecastWeather(response)

                    if (weatherForecast != null) {
                        for (i in 0 until weatherForecast.numRecords) {
                            weatherForecast.getForecast(i)?.let { lwRecords.add(it) }
                        }
                    }
                    weatherRecords.postValue(lwRecords)
                },
                { error ->

                }
        )
        stringRequest.retryPolicy = policy
        requestQueue.add(stringRequest)
    }

}