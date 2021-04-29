package com.raantech.solalat.provider.data.models.product.response.product

import com.google.gson.annotations.SerializedName

data class Price(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("formatted")
	val formatted: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null
)