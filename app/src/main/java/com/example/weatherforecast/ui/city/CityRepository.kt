package com.example.weatherforecast.ui.city

import com.example.weatherforecast.network.ApiService
import com.example.weatherforecast.network.response.WeatherResponse
import retrofit2.Response

/**
 * Created by Harish on 16-07-2021
 */
class CityRepository {

    suspend fun getWeatherData(
        units: String,
        lat: String,
        lon: String,
        apiKey: String
    ): Response<WeatherResponse> {

        return ApiService().getWeatherData(units,lat, lon, apiKey)
    }

}