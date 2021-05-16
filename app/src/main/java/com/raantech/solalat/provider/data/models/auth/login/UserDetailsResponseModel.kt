package com.raantech.solalat.provider.data.models.auth.login

import com.google.gson.annotations.SerializedName

data class UserDetailsResponseModel(

	@field:SerializedName("user_info")
	val userInfo: UserInfo? = null,

	@field:SerializedName("auth_token")
	val authToken: String? = null,

	@field:SerializedName("has_iban")
	var hasIban: Boolean? = false
)