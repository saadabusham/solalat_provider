package com.raantech.solalat.provider.data.models.product.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Files(

	@field:SerializedName("additional_images")
	val additionalImages: List<Int>? = null,

	@field:SerializedName("base_image")
	val baseImage: Int? = null
):Serializable