package com.raantech.solalat.provider.data.models.product.response

import com.google.gson.annotations.SerializedName

data class ServiceCategoriesResponse(

	@field:SerializedName("categories")
	val categories: List<ServiceCategory>? = null
)