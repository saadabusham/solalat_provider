package com.raantech.solalat.provider.data.daos.remote.medical

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import retrofit2.http.*

interface MedicalRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/products/medical")
    suspend fun getProducts(
            @Query("skip") skip: Int
    ): ResponseWrapper<List<Medical>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("provider/products/medical/store")
    suspend fun addProduct(
            @Body addProductRequest: AddMedicalRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("provider/products/medical/{id}/update")
    suspend fun updateProduct(
            @Path("id") id: Int,
            @Body addProductRequest: AddMedicalRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("provider/media/{mediaId}/destroy")
    suspend fun deleteMedia(
            @Path("mediaId") mediaId: Int
    ): ResponseWrapper<Any>


}