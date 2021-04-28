package com.raantech.solalat.provider.data.models.media

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media(

	@field:SerializedName("extension")
	val extension: String,

	@field:SerializedName("filename")
	val filename: String,

	@field:SerializedName("size")
	val size: String,

	@field:SerializedName("mime")
	val mime: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("url")
	val url: String
):Serializable