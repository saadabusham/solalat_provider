package com.raantech.solalat.provider.data.models.main.home

import com.google.gson.annotations.SerializedName
import com.raantech.solalat.provider.data.models.product.response.product.Price

data class MyService(

	@field:SerializedName("calls")
	val calls: Int? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("viewed")
	val viewed: Int? = null,

	@field:SerializedName("account_balance")
	val account_balance: Price? = null
){

}