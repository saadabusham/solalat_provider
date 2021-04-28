package com.raantech.solalat.provider.data.repos.media

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.media.Media
import okhttp3.MultipartBody
import retrofit2.http.Part
import retrofit2.http.Path

interface MediaRepo {
    suspend fun getMedia(
            skip: Int
    ): APIResource<ResponseWrapper<List<Media>>>

    suspend fun uploadMedia(
        mediaFile : MultipartBody.Part
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteMedia(
        mediaId: Int
    ): APIResource<ResponseWrapper<Any>>
}