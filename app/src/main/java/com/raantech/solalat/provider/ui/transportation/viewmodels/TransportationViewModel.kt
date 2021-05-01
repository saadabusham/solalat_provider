package com.raantech.solalat.provider.ui.transportation.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.product.request.Files
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.models.transportation.request.AddTransportationRequest
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.product.ProductsRepo
import com.raantech.solalat.provider.data.repos.transportation.TransportationRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.concatStrings

class TransportationViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    val transportationRepo: TransportationRepo,
    private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {

    var addNew:Boolean = false
    val productName: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: MutableLiveData<Address> = MutableLiveData()
    val addressString: MutableLiveData<String> = MutableLiveData()
    val categoryId: MutableLiveData<Int> = MutableLiveData()

    fun getTransportation(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = transportationRepo.getTransportation(skip)
        emit(response)
    }

    fun getAccessoriesCategories() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getServiceCategories(ServiceTypesEnum.TRANSPORTATION.value)
        emit(response)
    }

    fun addTransportation(files: List<Media>, serviceCategory: ServiceCategory,receivedWhatsapp:Boolean) = liveData {
        emit(APIResource.loading())
        val response = transportationRepo.addTransportation(getAddProductRequest(files.map { it.id },serviceCategory.id,receivedWhatsapp))
        emit(response)
    }

    private fun getAddProductRequest(files: List<Int>, categoryId: Int?, receivedWhatsapp: Boolean): AddTransportationRequest {
        return AddTransportationRequest(
            isActive = true,
            categoryId = categoryId,
            latitude = address.value?.lat,
            longitude = address.value?.lon,
            address = addressString.value.toString(),
            name = productName.value,
            contactNumber = phoneNumber.value.toString().checkPhoneNumberFormat()
                .concatStrings(selectedCountryCode.value.toString()),
            files = Files(baseImage = files[0], additionalImages = files),
            receivedWhatsapp = receivedWhatsapp
        )
    }
}