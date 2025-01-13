package com.excercise.growme

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic colors to all activities
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}