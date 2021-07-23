package com.example.weatherforecast.network.response

import com.squareup.moshi.Json

/**
 * Created by Harish on 17-07-2021
 */
data class Rain(@Json(name = "3h") val threeHourlyVolume: Double)