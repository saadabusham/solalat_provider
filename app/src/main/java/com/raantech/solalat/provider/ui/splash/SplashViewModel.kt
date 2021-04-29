package com.raantech.solalat.provider.ui.splash

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.UserEnums
import com.raantech.solalat.provider.data.repos.user.UserRepo
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.pref.SharedPreferencesUtil

class SplashViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepo,
    private val configurationRepo: ConfigurationRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseViewModel() {

    fun getConfigurationData() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.loadConfigurationData()
        emit(response)
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
}