package com.raantech.solalat.provider.ui.more.aboutus

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.solalat.provider.data.pref.configuration.ConfigurationPref
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.pref.SharedPreferencesUtil

class AboutUsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    val sharedPreferencesUtil: SharedPreferencesUtil,
    val configurationPref: ConfigurationPref,
    private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {

    fun getAboutUs() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getAboutUs()
        emit(response)
    }


    fun getSocialMedia(): ConfigurationWrapperResponse? {
        return sharedPreferencesUtil.getConfigurationPreferences()
    }

}