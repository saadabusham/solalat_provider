package com.raantech.solalat.provider.data.repos.medical

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest

interface MedicalRepo {
    suspend fun getMedicals(
        skip: Int
    ): APIResource<ResponseWrapper<List<Medical>>>

    suspend fun addMedicalService(
        addProductRequest: AddMedicalRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteMedia(
        mediaId: Int
    ): APIResource<ResponseWrapper<Any>>
}