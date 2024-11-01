package com.example.mydoctor.di

import com.example.mydoctor.utils.Resource
import com.example.mydoctor.utils.ResourceManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {

    @Binds
    abstract fun bindResourceManager(resourceManager: ResourceManager): Resource

}