package com.raantech.solalat.provider.data.models.medical.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddMedicalRequest(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("contact_number")
	val contactNumber: String? = null,

	@field:SerializedName("received_whatsapp")
	val receivedWhatsapp: Boolean? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
) : Serializable