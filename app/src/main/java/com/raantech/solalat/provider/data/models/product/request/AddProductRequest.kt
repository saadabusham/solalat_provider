package com.raantech.solalat.provider.data.models.product.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddProductRequest(

		@field:SerializedName("is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("category_id")
		val categoryId: Int? = null,

		@field:SerializedName("price")
		val price: Double? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("files")
		val files: Files? = null,

		@field:SerializedName("contact_number")
		val contactNumber: String? = null,

		@field:SerializedName("received_whatsapp")
		val receivedWhatsapp: Boolean? = null,

		@field:SerializedName("is_available")
		val isAvailable: Boolean? = null,

		@field:SerializedName("date_of_availability")
		val dateOfAvailability: String? = null
) : Serializable