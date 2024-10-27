package com.example.mydoctor.di

import android.content.Context
import com.example.mydoctor.data.local.PressureDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        PressureDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providePlaceDao(database: PressureDatabase) =
        database.getPressure()
}