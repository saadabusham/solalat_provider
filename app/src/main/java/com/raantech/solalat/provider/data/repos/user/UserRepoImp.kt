package com.raantech.solalat.provider.data.repos.user

import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.user.UserRemoteDao
import com.raantech.solalat.provider.data.enums.UserEnums
import com.raantech.solalat.provider.data.models.auth.login.TokenModel
import com.raantech.solalat.provider.data.models.auth.login.UserDetailsResponseModel
import com.raantech.solalat.provider.data.models.main.home.MyService
import com.raantech.solalat.provider.data.pref.user.UserPref
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class UserRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val userRemoteDao: UserRemoteDao,
    private val userPref: UserPref
) : BaseRepo(responseHandler), UserRepo {


    override suspend fun login(
        phoneNumber: String
    ): APIResource<ResponseWrapper<TokenModel>> {
        return try {
            responseHandle.handleSuccess(userRemoteDao.login(
                phoneNumber
            ))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getMyServices(): APIResource<ResponseWrapper<List<MyService>>> {
        return try {
            responseHandle.handleSuccess(userRemoteDao.getMyServices(
            ))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun resendCode(token: String): APIResource<ResponseWrapper<TokenModel>> {
        return try {
            responseHandle.handleSuccess(userRemoteDao.resendCode(
                token
            ))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun verify(
        token: String,
        code: Int,
        device_token: String,
        platform: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(userRemoteDao.verify(
                token, code, device_token, platform
            ))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override fun saveAccessToken(accessToken: String) {
        userPref.saveAccessToken(accessToken)
    }

    override fun getAccessToken(): String {
        return userPref.getAccessToken()
    }

    override fun saveUserPassword(password: String) {
        userPref.saveUserPassword(password)
    }

    override fun getUserPassword(): String {
        return userPref.getUserPassword()
    }

    override fun setUserStatus(userState: UserEnums.UserState) {
        userPref.setUserStatus(userState.ordinal)
    }

    override fun getUserStatus(): UserEnums.UserState {
        return UserEnums.UserState.getUserStateByPosition(userPref.getUserStatus())
    }


    override fun saveNotificationStatus(flag: Boolean) {
        userPref.setNotificationStatus(flag)
    }

    override fun getNotificationStatus(): Boolean {
        return userPref.getNotificationStatus()
    }

    override fun saveTouchIdStatus(flag: Boolean) {
        userPref.setTouchIdStatus(flag)
    }

    override fun getTouchIdStatus(): Boolean {
        return userPref.getTouchIdStatus()
    }

    override fun setUser(user: UserDetailsResponseModel) {
        userPref.setUser(user)
    }

    override fun getUser(): UserDetailsResponseModel? {
        return userPref.getUser()
    }

}