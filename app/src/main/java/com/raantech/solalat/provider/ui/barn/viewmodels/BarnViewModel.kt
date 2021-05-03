package com.raantech.solalat.provider.ui.barn.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.barn.request.AddBarnRequest
import com.raantech.solalat.provider.data.models.barn.respnose.ServicesItem
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.request.Files
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.data.repos.barn.BarnRepo
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.transportation.TransportationRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.concatStrings
import org.intellij.lang.annotations.Language

class BarnViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        val barnRepo: BarnRepo,
        private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {

    var addNew: Boolean = false
    val productName: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val price: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: MutableLiveData<Address> = MutableLiveData()
    val addressString: MutableLiveData<String> = MutableLiveData()
    var services: MutableList<ServicesItem> = mutableListOf()
    val files : MutableList<Media> = mutableListOf()
    val logo : MutableList<Media> = mutableListOf()
    fun getBarns(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = barnRepo.getBarns(skip)
        emit(response)
    }

    fun getBarnServices() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getBarnServices()
        emit(response)
    }

    fun addBarn(addBarnRequest: AddBarnRequest) = liveData {
        emit(APIResource.loading())
        val response = barnRepo.addBarns(addBarnRequest)
        emit(response)
    }

    fun getBarnRequest(receivedWhatsapp: Boolean): AddBarnRequest {
        return AddBarnRequest(
                isActive = true,
                address = addressString.value.toString(),
                latitude = address.value?.lat,
                longitude = address.value?.lon,
                contactNumber = phoneNumber.value.toString().checkPhoneNumberFormat()
                        .concatStrings(selectedCountryCode.value.toString()),
                name = productName.value,
                files = Files(baseImage = logo[0].id, additionalImages = files.map { it.id }),
                receivedWhatsapp = receivedWhatsapp,
                price = price.value.toString().toDouble(),
                description = description.value.toString(),
                services = services.map { it.id }
        )
    }
}