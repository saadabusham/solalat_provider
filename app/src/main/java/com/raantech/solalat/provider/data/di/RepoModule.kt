package com.raantech.solalat.provider.data.di


import com.raantech.solalat.provider.data.repos.user.UserRepo
import com.raantech.solalat.provider.data.repos.user.UserRepoImp
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepoImp
import com.raantech.solalat.provider.data.repos.media.MediaRepo
import com.raantech.solalat.provider.data.repos.media.MediaRepoImp
import com.raantech.solalat.provider.data.repos.medical.MedicalRepo
import com.raantech.solalat.provider.data.repos.medical.MedicalRepoImp
import com.raantech.solalat.provider.data.repos.product.ProductsRepo
import com.raantech.solalat.provider.data.repos.product.ProductsRepoImp
import com.raantech.solalat.provider.data.repos.transportation.TransportationRepo
import com.raantech.solalat.provider.data.repos.transportation.TransportationRepoImp
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
    abstract fun bindUserRepo(userRepoImp: UserRepoImp): UserRepo

    @Singleton
    @Binds
    abstract fun bindMediaRepo(mediaRepoImp: MediaRepoImp): MediaRepo


    @Singleton
    @Binds
    abstract fun bindProductsRepo(productsRepoImp: ProductsRepoImp): ProductsRepo

    @Singleton
    @Binds
    abstract fun bindMedicalRepo(medicalRepoImp: MedicalRepoImp): MedicalRepo

    @Singleton
    @Binds
    abstract fun bindTransportationRepo(transportationRepoImp: TransportationRepoImp): TransportationRepo


}