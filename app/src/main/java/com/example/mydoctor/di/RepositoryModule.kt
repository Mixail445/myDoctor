package com.example.mydoctor.di

import com.example.mydoctor.data.local.PressureDao
import com.example.mydoctor.data.local.PressureLocalSourceImpl
import com.example.mydoctor.domain.PressureLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}