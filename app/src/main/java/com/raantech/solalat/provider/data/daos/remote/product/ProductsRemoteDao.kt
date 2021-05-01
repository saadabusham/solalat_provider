package com.raantech.solalat.provider.data.daos.remote.product

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.product.Product
import retrofit2.http.*

interface ProductsRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("provider/products/accessories")
    suspend fun getProducts(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Product>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("categories")
    suspend fun getServiceCategories(
        @Query("type") type: String
    ): ResponseWrapper<ServiceCategoriesResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("provider/products/accessories/store")
    suspend fun addProduct(
        @Body addProductRequest: AddProductRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("provider/media/{mediaId}/destroy")
    suspend fun deleteMedia(
        @Path("mediaId") mediaId: Int
    ): ResponseWrapper<Any>


}