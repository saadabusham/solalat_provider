package com.raantech.solalat.provider.data.models.auth.login

import com.google.gson.annotations.SerializedName

data class UserInfo(

	@field:SerializedName("is_blocked")
	val isBlocked: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("last_login")
	val lastLogin: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)