package com.raantech.solalat.provider.data.repos.transportation

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.medical.MedicalRemoteDao
import com.raantech.solalat.provider.data.daos.remote.transportation.TransportationRemoteDao
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class TransportationRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val transportationRemoteDao: TransportationRemoteDao
) : BaseRepo(responseHandler), TransportationRepo {

    override suspend fun getTransportation(skip: Int): APIResource<ResponseWrapper<List<Transportation>>> {
        return try {
            responseHandle.handleSuccess(transportationRemoteDao.getTransportation(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addTransportation(addTransportationRequest: AddTransportationRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(transportationRemoteDao.addTransportation(addTransportationRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateTransportation(
        id: Int,
        addTransportationRequest: AddTransportationRequest
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(transportationRemoteDao.updateTransportation(id,addTransportationRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}