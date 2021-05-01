package com.raantech.solalat.provider.data.daos.remote.configuration

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.more.AboutUsResponse
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.more.FaqsResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ConfigurationRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("app/settings")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("app/aboutUs")
    suspend fun getAboutUs(): ResponseWrapper<AboutUsResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/media")
    suspend fun getFaqs(
        @Query("skip") skip: Int,
        @Query("type") type: String
    ): ResponseWrapper<List<FaqsResponse>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("categories")
    suspend fun getServiceCategories(
        @Query("type") type: String
    ): ResponseWrapper<ServiceCategoriesResponse>
}