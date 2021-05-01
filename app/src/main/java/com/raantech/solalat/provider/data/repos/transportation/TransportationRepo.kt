package com.raantech.solalat.provider.data.repos.transportation

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import retrofit2.http.Body
import retrofit2.http.Path

interface TransportationRepo {
    suspend fun getTransportation(
        skip: Int
    ): APIResource<ResponseWrapper<List<Transportation>>>

    suspend fun addTransportation(
        addTransportationRequest: AddTransportationRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateTransportation(
        id:Int,
        addTransportationRequest:AddTransportationRequest
    ): APIResource<ResponseWrapper<Any>>
}