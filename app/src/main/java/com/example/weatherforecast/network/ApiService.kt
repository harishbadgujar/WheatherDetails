package com.example.weatherforecast.network

import com.example.weatherforecast.BASE_URL
import com.example.weatherforecast.network.response.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Harish on 17-07-2021
 */
interface ApiService {

    @GET("weather")
    suspend fun getWeatherData(@Query("units") units: String,
                               @Query("lat") lat: String,
                               @Query("lon") lon: String,
                               @Query("appid") apiKey: String):Response<WeatherResponse>

    companion object {

        operator fun invoke(): ApiService{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY


            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create(ApiService::class.java)
        }
    }

}