package com.raantech.solalat.provider.data.repos.barn

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.barn.request.AddBarnRequest
import com.raantech.solalat.provider.data.models.barn.respnose.Barn

interface BarnRepo {
    suspend fun getBarns(
            skip: Int
    ): APIResource<ResponseWrapper<List<Barn>>>

    suspend fun addBarns(
            addBarnRequest: AddBarnRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateBarns(
            id: Int,
            addBarnRequest: AddBarnRequest
    ): APIResource<ResponseWrapper<Any>>
}