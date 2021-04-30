package com.raantech.solalat.provider.ui.main.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.common.CommonEnums
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.UserEnums
import com.raantech.solalat.provider.data.models.main.home.MyService
import com.raantech.solalat.provider.data.pref.configuration.ConfigurationPref
import com.raantech.solalat.provider.data.pref.user.UserPref
import com.raantech.solalat.provider.data.repos.product.ProductsRepo
import com.raantech.solalat.provider.data.repos.user.UserRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.pref.SharedPreferencesUtil

class MainViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        private val userRepo: UserRepo,
        private val sharedPreferencesUtil: SharedPreferencesUtil,
        private val userPref: UserPref,
        private val configurationPref: ConfigurationPref,
        private val productsRepo: ProductsRepo
) : BaseViewModel() {

    val currentService: MutableLiveData<MyService> = MutableLiveData()
    val myServices : MutableList<MyService> = mutableListOf()
    fun logout() {
        if (userRepo.getTouchIdStatus())
            sharedPreferencesUtil.logout()
        else {
            sharedPreferencesUtil.clearPreference()
            userPref.setIsFirstOpen(false)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
    }

    fun saveLanguage() = liveData {
        configurationPref.setAppLanguageValue(if (configurationPref.getAppLanguageValue() == "ar") CommonEnums.Languages.English.value else CommonEnums.Languages.Arabic.value)
        emit(null)
    }
    fun getAppLanguage(): String {
        return configurationPref.getAppLanguageValue()
    }

    fun getMyServices() = liveData {
        emit(APIResource.loading())
        val response = userRepo.getMyServices()
        emit(response)
    }

    fun getProducts(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = productsRepo.getProducts(skip)
        emit(response)
    }

}