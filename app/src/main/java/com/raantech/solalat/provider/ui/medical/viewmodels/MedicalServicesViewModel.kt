package com.raantech.solalat.provider.ui.medical.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.medical.request.AddMedicalRequest
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.repos.configuration.ConfigurationRepo
import com.raantech.solalat.provider.data.repos.medical.MedicalRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.concatStrings

class MedicalServicesViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        val medicalRepo: MedicalRepo,
        private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {
    var medicalToUpdate: Medical? = null
    var addNew: Boolean = false
    val productName: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()
    val addressString: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: MutableLiveData<Address> = MutableLiveData()
    fun getMedicals(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = medicalRepo.getMedicals(skip)
        emit(response)
    }

    fun getServicesCategories() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getServiceCategories(ServiceTypesEnum.MEDICAL.value)
        emit(response)
    }

    fun addMedical(serviceCategory: ServiceCategory, receivedWhatsapp: Boolean, isActive: Boolean) = liveData {
        emit(APIResource.loading())
        val response = medicalRepo.addMedicalService(
                getAddProductRequest(
                        serviceCategory.id,
                        receivedWhatsapp,
                        isActive
                )
        )
        emit(response)
    }

    fun updateMedical(serviceCategory: ServiceCategory, receivedWhatsapp: Boolean, isActive: Boolean) = liveData {
        emit(APIResource.loading())
        val response = medicalRepo.updateMedicalService(
                medicalToUpdate!!.id!!,
                getAddProductRequest(
                        serviceCategory.id,
                        receivedWhatsapp,
                        isActive
                )
        )
        emit(response)
    }

    private fun getAddProductRequest(
            categoryId: Int?,
            receivedWhatsapp: Boolean,
            isActive: Boolean
    ): AddMedicalRequest {
        return AddMedicalRequest(
                isActive = isActive,
                categoryId = categoryId,
                address = addressString.value.toString(),
                latitude = address.value?.lat,
                longitude = address.value?.lon,
                name = productName.value,
                description = productDescription.value,
                contactNumber = phoneNumber.value.toString().checkPhoneNumberFormat()
                        .concatStrings(selectedCountryCode.value.toString()),
                receivedWhatsapp = receivedWhatsapp
        )
    }
}