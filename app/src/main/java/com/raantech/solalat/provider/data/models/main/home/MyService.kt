package com.raantech.solalat.provider.data.models.main.home

import com.google.gson.annotations.SerializedName

data class MyService(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("calls")
	val calls: Int? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("viewed")
	val viewed: Int? = null
)