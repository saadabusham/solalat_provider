package com.raantech.solalat.provider.data.repos.product

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.product.Product

interface ProductsRepo {
    suspend fun getProducts(
        skip: Int
    ): APIResource<ResponseWrapper<List<Product>>>

    suspend fun getServiceCategories(
        type: String
    ): APIResource<ResponseWrapper<ServiceCategoriesResponse>>

    suspend fun addProduct(
        addProductRequest: AddProductRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteMedia(
        mediaId: Int
    ): APIResource<ResponseWrapper<Any>>
}