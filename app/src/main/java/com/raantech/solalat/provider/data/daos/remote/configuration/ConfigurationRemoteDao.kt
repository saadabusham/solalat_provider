package com.raantech.solalat.provider.data.daos.remote.configuration

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ConfigurationRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("app/settings")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>
}