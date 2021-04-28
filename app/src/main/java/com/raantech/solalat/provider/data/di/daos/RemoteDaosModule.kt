package com.raantech.solalat.provider.data.di.daos

import com.raantech.solalat.provider.data.daos.remote.configuration.ConfigurationRemoteDao
import com.raantech.solalat.provider.data.daos.remote.media.MediaRemoteDao
import com.raantech.solalat.provider.data.daos.remote.user.UserRemoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RemoteDaosModule {


    @Singleton
    @Provides
    fun provideUserRemoteDao(
        retrofit: Retrofit
    ): UserRemoteDao {
        return retrofit.create(UserRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideConfigurationRemoteDao(
        retrofit: Retrofit
    ): ConfigurationRemoteDao {
        return retrofit.create(ConfigurationRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideMediaRemoteDao(
        retrofit: Retrofit
    ): MediaRemoteDao {
        return retrofit.create(MediaRemoteDao::class.java)
    }

}