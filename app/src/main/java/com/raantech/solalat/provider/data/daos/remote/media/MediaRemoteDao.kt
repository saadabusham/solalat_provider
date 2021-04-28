package com.raantech.solalat.provider.data.daos.remote.media

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.media.Media
import okhttp3.MultipartBody
import retrofit2.http.*

interface MediaRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/media")
    suspend fun getMedia(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Media>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("provider/media/store")
    suspend fun uploadMedia(
        @Part mediaFile: MultipartBody.Part
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("provider/media/{mediaId}/destroy")
    suspend fun deleteMedia(
        @Path("mediaId") mediaId: Int
    ): ResponseWrapper<Any>


}