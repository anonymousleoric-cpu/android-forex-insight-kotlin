package com.example.forexinsight

import android.app.Application
import androidx.room.Room
import com.example.forexinsight.data.db.AppDatabase

class ForexInsightApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "forex_insight.db").build()
    }
}
