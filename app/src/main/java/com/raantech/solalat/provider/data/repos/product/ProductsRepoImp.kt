package com.raantech.solalat.provider.data.repos.product

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.product.ProductsRemoteDao
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.product.response.product.Product
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class ProductsRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val productsRemoteDao: ProductsRemoteDao
) : BaseRepo(responseHandler), ProductsRepo {

    override suspend fun getProducts(skip: Int): APIResource<ResponseWrapper<List<Product>>> {
        return try {
            responseHandle.handleSuccess(productsRemoteDao.getProducts(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addProduct(addProductRequest: AddProductRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(productsRemoteDao.addProduct(addProductRequest))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }


    override suspend fun deleteMedia(mediaId: Int): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(productsRemoteDao.deleteMedia(mediaId))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}