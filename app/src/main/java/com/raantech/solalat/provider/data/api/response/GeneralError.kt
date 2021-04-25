package com.raantech.solalat.provider.data.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.StringBuilder

data class GeneralError(

	@field:SerializedName("key")
	val key: String,

	@field:SerializedName("errors")
	val errors: List<String>?
):Serializable{
	fun getErrorsString(): String {
		val errorBuilder = StringBuilder()
		errors?.withIndex()?.forEach {
			errorBuilder.append(it.value + if(it.index != errors.size) "\n" else "")
		}
		return errorBuilder.toString()
	}
}