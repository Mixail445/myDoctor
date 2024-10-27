package com.example.mydoctor.di

import com.example.mydoctor.data.local.PressureDao
import com.example.mydoctor.data.local.PressureLocalSourceImpl
import com.example.mydoctor.domain.PressureLocalSource
import com.example.mydoctor.utils.DispatchersProvider
import com.example.mydoctor.utils.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideDispatcher(): DispatchersProvider {
        return DispatchersProviderImpl
    }

    @Provides
    @Singleton
    fun provideLocalSource(
        dao: PressureDao, dispatchersProvider: DispatchersProvider
    ): PressureLocalSource = PressureLocalSourceImpl(dao, dispatchersProvider)

}