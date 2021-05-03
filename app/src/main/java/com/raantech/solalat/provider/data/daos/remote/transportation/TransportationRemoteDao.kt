package com.raantech.solalat.provider.data.daos.remote.transportation

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import retrofit2.http.*

interface TransportationRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/products/truck")
    suspend fun getTransportation(
            @Query("skip") skip: Int
    ): ResponseWrapper<List<Transportation>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("provider/products/truck/store")
    suspend fun addTransportation(
            @Body addTransportationRequest: AddTransportationRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("provider/products/truck/{id}/update")
    suspend fun updateTransportation(
            @Path("id") id: Int,
            @Body addTransportationRequest: AddTransportationRequest
    ): ResponseWrapper<Any>

}