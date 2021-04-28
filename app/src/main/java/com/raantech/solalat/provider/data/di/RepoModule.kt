package com.raantech.solalat.provider.data.di


import com.raantech.solalat.provider.data.repos.auth.UserRepo
import com.raantech.solalat.provider.data.repos.auth.UserRepoImp
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepoImp
import com.raantech.solalat.provider.data.repos.media.MediaRepo
import com.raantech.solalat.provider.data.repos.media.MediaRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindConfigurationRepo(configurationRepoImp: ConfigurationRepoImp): ConfigurationRepo

    @Singleton
    @Binds
    abstract fun bindUserRepo(userRepoImp: UserRepoImp) : UserRepo

    @Singleton
    @Binds
    abstract fun bindMediaRepo(mediaRepoImp: MediaRepoImp) : MediaRepo



}