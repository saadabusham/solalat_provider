package com.raantech.solalat.provider.data.daos.remote.barn

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.barn.request.AddBarnRequest
import com.raantech.solalat.provider.data.models.barn.respnose.Barn
import retrofit2.http.*

interface BarnRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/products/stable")
    suspend fun getBarns(
            @Query("skip") skip: Int
    ): ResponseWrapper<List<Barn>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("provider/products/stable/store")
    suspend fun addBarn(
            @Body addBarnRequest: AddBarnRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("provider/products/stable/{id}/update")
    suspend fun updateBarn(
            @Path("id") id: Int,
            @Body addBarnRequest: AddBarnRequest
    ): ResponseWrapper<Any>

}