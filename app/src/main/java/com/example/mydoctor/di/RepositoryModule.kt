package com.example.mydoctor.di

import android.content.Context
import com.example.mydoctor.data.local.PressureDao
import com.example.mydoctor.data.local.PressureLocalSourceImpl
import com.example.mydoctor.domain.PressureLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalSource(
        dao: PressureDao,
    ): PressureLocalSource = PressureLocalSourceImpl(dao)

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}