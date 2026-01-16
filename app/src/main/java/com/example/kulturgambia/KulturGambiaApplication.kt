package com.example.kulturgambia

import android.app.Application
import com.example.kulturgambia.data.AppInstance

class KulturGambiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInstance.init(this)
    }
}
