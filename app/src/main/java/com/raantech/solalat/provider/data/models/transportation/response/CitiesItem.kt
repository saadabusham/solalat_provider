package com.raantech.solalat.provider.data.models.transportation.response

import com.google.gson.annotations.SerializedName

data class CitiesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)