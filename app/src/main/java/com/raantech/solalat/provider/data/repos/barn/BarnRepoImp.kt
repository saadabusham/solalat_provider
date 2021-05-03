package com.raantech.solalat.provider.data.repos.barn

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.barn.BarnRemoteDao
import com.raantech.solalat.provider.data.models.barn.request.AddBarnRequest
import com.raantech.solalat.provider.data.models.barn.respnose.Barn
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class BarnRepoImp @Inject constructor(
        responseHandler: ResponseHandler,
        private val barnRemoteDao: BarnRemoteDao
) : BaseRepo(responseHandler), BarnRepo {

    override suspend fun getBarns(skip: Int): APIResource<ResponseWrapper<List<Barn>>> {
        return try {
            responseHandle.handleSuccess(barnRemoteDao.getBarns(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addBarns(addBarnRequest: AddBarnRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(barnRemoteDao.addBarn(addBarnRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateBarns(id: Int, addBarnRequest: AddBarnRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(barnRemoteDao.updateBarn(id, addBarnRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}