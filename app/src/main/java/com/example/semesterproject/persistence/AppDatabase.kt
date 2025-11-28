package com.example.semesterproject.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.semesterproject.persistence.dao.ForecastDao
import com.example.semesterproject.persistence.entities.Current
import com.example.semesterproject.persistence.entities.ForecastResponse
import com.example.semesterproject.persistence.entities.Location

@Database(
    entities = [Current::class, Location::class, ForecastResponse::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "forecast_db"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}