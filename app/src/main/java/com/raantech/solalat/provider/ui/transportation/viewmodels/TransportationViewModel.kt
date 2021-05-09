package com.raantech.solalat.provider.ui.transportation.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.product.request.Files
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.transportation.TransportationRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.concatStrings

class TransportationViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        val transportationRepo: TransportationRepo,
        private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {
    var transpornToEdit: Transportation? = null
    var addNew: Boolean = false
    val productName: MutableLiveData<String> = MutableLiveData()
    val plateNumber: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: MutableLiveData<Address> = MutableLiveData()
    val addressString: MutableLiveData<String> = MutableLiveData()
    var cities: MutableList<City> = mutableListOf()
    fun getTransportation(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = transportationRepo.getTransportation(skip)
        emit(response)
    }

    fun getServicesCategories() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getServiceCategories(ServiceTypesEnum.TRANSPORTATION.value)
        emit(response)
    }

    fun getCities() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCities()
        emit(response)
    }

    fun addTransportation(addTransportationRequest: AddTransportationRequest) = liveData {
        emit(APIResource.loading())
        val response = transportationRepo.addTransportation(addTransportationRequest)
        emit(response)
    }

    fun updateTransportation(addTransportationRequest: AddTransportationRequest) = liveData {
        emit(APIResource.loading())
        val response = transportationRepo.updateTransportation(transpornToEdit!!.id!!, addTransportationRequest)
        emit(response)
    }

    fun getTransportationRequest(files: List<Int>,
                                 categoryId: Int?,
                                 year: Int,
                                 receivedWhatsapp: Boolean,
                                 globalTransportation: Boolean,
                                 isActive: Boolean): AddTransportationRequest {
        return AddTransportationRequest(
                isActive = isActive,
                address = addressString.value.toString(),
                latitude = address.value?.lat,
                longitude = address.value?.lon,
                truckNumber = plateNumber.value?.toString(),
                contactNumber = phoneNumber.value.toString().checkPhoneNumberFormat()
                        .concatStrings(selectedCountryCode.value.toString()),
                manufacturingYear = year,
                categoryId = categoryId,
                name = productName.value,
                files = Files(baseImage = files[0], additionalImages = files),
                receivedWhatsapp = receivedWhatsapp,
                availableIt = globalTransportation,
                cities = cities.map { it.id }
        )
    }
}