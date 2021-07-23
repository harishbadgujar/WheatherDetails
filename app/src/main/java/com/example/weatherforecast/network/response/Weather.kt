package com.example.weatherforecast.network.response

/**
 * Created by Harish on 17-07-2021
 */
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)