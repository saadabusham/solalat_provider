package com.raantech.solalat.provider.data.models.barn.respnose

import com.google.gson.annotations.SerializedName
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.response.product.Price

data class Barn(
		@field:SerializedName("additional_images")
		val additionalImages: List<Media>? = null,

		@field:SerializedName("is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("address")
		val address: String? = null,

		@field:SerializedName("distance")
		val distance: Int? = null,

		@field:SerializedName("latitude")
		val latitude: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("services")
		val services: List<ServicesItem>? = null,

		@field:SerializedName("contact_number")
		val contactNumber: String? = null,

		@field:SerializedName("base_image")
		val baseImage: Media? = null,

		@field:SerializedName("price")
		val price: Price? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("is_approved")
		val isApproved: Boolean? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("received_whatsapp")
		val receivedWhatsapp: Boolean? = null,

		@field:SerializedName("longitude")
		val longitude: String? = null
)
