package com.raantech.solalat.provider.data.repos.medical

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.medical.MedicalRemoteDao
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class MedicalRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val medicalRemoteDao: MedicalRemoteDao
) : BaseRepo(responseHandler), MedicalRepo {

    override suspend fun getMedicals(skip: Int): APIResource<ResponseWrapper<List<Medical>>> {
        return try {
            responseHandle.handleSuccess(medicalRemoteDao.getProducts(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addMedicalService(addProductRequest: AddMedicalRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(medicalRemoteDao.addProduct(addProductRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }


    override suspend fun deleteMedia(mediaId: Int): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(medicalRemoteDao.deleteMedia(mediaId))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}