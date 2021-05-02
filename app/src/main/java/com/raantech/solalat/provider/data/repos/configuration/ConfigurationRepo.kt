package com.raantech.solalat.provider.data.repos.configuration

import com.raantech.solalat.provider.common.CommonEnums
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.solalat.provider.data.models.more.AboutUsResponse
import com.raantech.solalat.provider.data.models.more.FaqsResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.transportation.response.City

interface ConfigurationRepo {

    fun setAppLanguage(selectedLanguage: CommonEnums.Languages)
    fun getAppLanguage(): CommonEnums.Languages

    suspend fun loadConfigurationData(): APIResource<ResponseWrapper<ConfigurationWrapperResponse>>
    suspend fun getAboutUs(): APIResource<ResponseWrapper<AboutUsResponse>>
    suspend fun getServiceCategories(
        type: String
    ): APIResource<ResponseWrapper<ServiceCategoriesResponse>>

    suspend fun getFaqs(
        skip: Int,
        type: String
    ): APIResource<ResponseWrapper<List<FaqsResponse>>>

    suspend fun getCities(
    ): APIResource<ResponseWrapper<List<City>>>
}