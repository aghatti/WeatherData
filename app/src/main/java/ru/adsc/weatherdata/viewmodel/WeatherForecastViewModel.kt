package ru.adsc.weatherdata.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.adsc.weatherdata.data.DataRepository
import ru.adsc.weatherdata.data.model.dDayForecast

class WeatherForecastViewModel(private val repository: DataRepository) : ViewModel() {
    val weatherRecords: LiveData<List<dDayForecast>> = repository.weatherRecords
    fun generateForecast(url: String) = viewModelScope.launch {
        repository.getWeatherForecast(url)
    }
    fun clearForecast() {
        repository.clearWeatherForecast()
    }

}

class WeatherForecastViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherForecastViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
