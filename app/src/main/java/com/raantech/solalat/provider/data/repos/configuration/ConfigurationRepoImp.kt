package com.raantech.solalat.provider.data.repos.configuration

import com.raantech.solalat.provider.common.CommonEnums
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.api.response.ResponseHandler
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.daos.remote.configuration.ConfigurationRemoteDao
import com.raantech.solalat.provider.data.models.more.AboutUsResponse
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.solalat.provider.data.models.more.FaqsResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.data.pref.configuration.ConfigurationPref
import com.raantech.solalat.provider.data.repos.base.BaseRepo
import javax.inject.Inject

class ConfigurationRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val configurationRemoteDao: ConfigurationRemoteDao,
    private val configurationPref: ConfigurationPref
) : BaseRepo(responseHandler), ConfigurationRepo {

    override fun setAppLanguage(selectedLanguage: CommonEnums.Languages) {
        configurationPref.setAppLanguageValue(selectedLanguage.value)
    }

    override fun getAppLanguage(): CommonEnums.Languages {
        return CommonEnums.Languages.getLanguageByValue(configurationPref.getAppLanguageValue())
    }

    override suspend fun loadConfigurationData():
            APIResource<ResponseWrapper<ConfigurationWrapperResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAppConfiguration())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAboutUs(): APIResource<ResponseWrapper<AboutUsResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAboutUs())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getFaqs(skip: Int, type: String): APIResource<ResponseWrapper<List<FaqsResponse>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getFaqs(type))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getCities(): APIResource<ResponseWrapper<List<City>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getCities())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }


    override suspend fun getServiceCategories(type: String): APIResource<ResponseWrapper<ServiceCategoriesResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getServiceCategories(type))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}