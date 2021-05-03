package com.raantech.solalat.provider.data.models.barn.request

import com.google.gson.annotations.SerializedName
import com.raantech.solalat.provider.data.models.product.request.Files
import java.io.Serializable

data class AddBarnRequest(

		@field:SerializedName("is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("address")
		val address: String? = null,

		@field:SerializedName("price")
		val price: Double? = null,

		@field:SerializedName("latitude")
		val latitude: Double? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("files")
		val files: Files? = null,

		@field:SerializedName("services")
		val services: List<Int>? = null,

		@field:SerializedName("contact_number")
		val contactNumber: String? = null,

		@field:SerializedName("received_whatsapp")
		val receivedWhatsapp: Boolean? = null,

		@field:SerializedName("longitude")
		val longitude: Double? = null
):Serializable