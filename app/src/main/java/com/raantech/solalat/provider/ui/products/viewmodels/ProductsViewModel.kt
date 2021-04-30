package com.raantech.solalat.provider.ui.products.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.request.AddProductRequest
import com.raantech.solalat.provider.data.models.product.request.Files
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.repos.product.ProductsRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.concatStrings

class ProductsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    val productsRepo: ProductsRepo
) : BaseViewModel() {

    val productName: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val categoryId: MutableLiveData<Int> = MutableLiveData()

    fun getProducts(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = productsRepo.getProducts(skip)
        emit(response)
    }

    fun getAccessoriesCategories() = liveData {
        emit(APIResource.loading())
        val response = productsRepo.getServiceCategories(ServiceTypesEnum.ACCESSORIES.value)
        emit(response)
    }

    fun addProduct(files: List<Media>, serviceCategory: ServiceCategory,receivedWhatsapp:Boolean) = liveData {
        emit(APIResource.loading())
        val response = productsRepo.addProduct(getAddProductRequest(files.map { it.id },serviceCategory.id,receivedWhatsapp))
        emit(response)
    }

    private fun getAddProductRequest(files: List<Int>, categoryId: Int?, receivedWhatsapp: Boolean): AddProductRequest {
        return AddProductRequest(
            isActive = true,
            categoryId = categoryId,
            price = productPrice.value.toString().toDouble(),
            name = productName.value,
            description = productDescription.value,
            contactNumber = phoneNumber.value.toString().checkPhoneNumberFormat()
                .concatStrings(selectedCountryCode.value.toString()),
            files = Files(baseImage = files[0], additionalImages = files),
            receivedWhatsapp = receivedWhatsapp
        )
    }
}