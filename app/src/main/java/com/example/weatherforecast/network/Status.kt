package com.example.weatherforecast.network

/**
 * Created by Harish on 19-07-2021
 */
sealed class Status {
    object Success : Status()
    object Error : Status()
    object Loading : Status()
}