package com.example.notebook22

import android.app.Application
import com.example.notebook22.data.AppDatabase

class BaseApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}