package com.example.weatherforecast.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.weatherforecast.OPEN_WEATHER_API_KEY
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.network.Resource
import java.lang.Exception

class CityViewModel : ViewModel() {

    fun getCityWeatherData(bookmark: Bookmark) = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = CityRepository().getWeatherData(units = "metric",lat =bookmark.lat, lon = bookmark.lon, apiKey = OPEN_WEATHER_API_KEY )))
        }catch (exception: Exception){
            emit(Resource.error(data = null, msg = exception.message?:"Error Occurred"))
        }
    }

}