package com.example.mydoctor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    [PressureEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PressureDatabase : RoomDatabase() {
    abstract fun getPressure(): PressureDao

    companion object {
        private const val DATABASE_NAME = "pressure_base"

        @Volatile
        private var INSTANCE: PressureDatabase? = null

        fun getDatabase(context: Context): PressureDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PressureDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
