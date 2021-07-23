package com.example.weatherforecast.database

import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar

/**
 * Created by Harish on 19-07-2021
 */
fun ContentLoadingProgressBar.showProgress(){
    isVisible = true
    animate()
}

fun ContentLoadingProgressBar.hideProgress(){
    isVisible = false
}