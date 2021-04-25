package com.raantech.solalat.provider.data.daos.remote.user

import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.NetworkConstants
import com.raantech.solalat.provider.data.models.auth.login.TokenModel
import com.raantech.solalat.provider.data.models.auth.login.UserDetailsResponseModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("provider/auth/login")
    suspend fun login(
        @Field("phone_number") phoneNumber: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("provider/auth/resend")
    suspend fun resendCode(
        @Field("token") token: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("provider/auth/verify")
    suspend fun verify(
        @Field("token") token: String,
        @Field("code") code: Int,
        @Field("device_token") device_token: String,
        @Field("platform") platform: String
    ): ResponseWrapper<UserDetailsResponseModel>

}