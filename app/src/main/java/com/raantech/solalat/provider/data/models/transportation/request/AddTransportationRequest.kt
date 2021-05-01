package com.raantech.solalat.provider.data.models.transportation.request

import com.google.gson.annotations.SerializedName
import com.raantech.solalat.provider.data.models.product.request.Files

data class AddTransportationRequest(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("cities")
	val cities: List<Int>? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("truck_number")
	val truckNumber: Int? = null,

	@field:SerializedName("contact_number")
	val contactNumber: String? = null,

	@field:SerializedName("manufacturing_year")
	val manufacturingYear: Int? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("available_it")
	val availableIt: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("files")
	val files: Files? = null,

	@field:SerializedName("received_whatsapp")
	val receivedWhatsapp: Boolean? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)