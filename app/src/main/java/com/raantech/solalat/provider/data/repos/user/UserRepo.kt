package com.raantech.solalat.provider.data.repos.user

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.enums.UserEnums
import com.raantech.solalat.provider.data.models.auth.login.TokenModel
import com.raantech.solalat.provider.data.models.auth.login.UserDetailsResponseModel
import com.raantech.solalat.provider.data.models.main.home.MyService


interface UserRepo {


    suspend fun login(
            phoneNumber: String
    ): APIResource<ResponseWrapper<TokenModel>>

    suspend fun logout(
    ): APIResource<ResponseWrapper<Any>>

    suspend fun getMyServices(

    ): APIResource<ResponseWrapper<List<MyService>>>

    suspend fun resendCode(
            token: String
    ): APIResource<ResponseWrapper<TokenModel>>

    suspend fun verify(
            token: String,
            code: Int,
            device_token: String,
            platform: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    fun saveNotificationStatus(flag: Boolean)
    fun getNotificationStatus(): Boolean

    fun saveTouchIdStatus(flag: Boolean)
    fun getTouchIdStatus(): Boolean

    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String

    fun saveUserPassword(password: String)
    fun getUserPassword(): String

    fun setUserStatus(userState: UserEnums.UserState)
    fun getUserStatus(): UserEnums.UserState

    fun setUser(user: UserDetailsResponseModel)
    fun getUser(): UserDetailsResponseModel?
}