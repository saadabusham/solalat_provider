package com.raantech.solalat.provider.data.models.product.response.product

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import java.io.Serializable

data class Product(

		@field:SerializedName("additional_images")
		val additionalImages: List<Media>? = null,

		@field:SerializedName("is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("numberـofـorders")
		val numberOfOrders: Int? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("contact_number")
		val contactNumber: String? = null,

		@field:SerializedName("base_image")
		val baseImage: Media? = null,

		@field:SerializedName("price")
		val price: Price? = null,

		@field:SerializedName("viewed")
		val viewed: Int? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("is_approved")
		val isApproved: Boolean? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("category")
		val category: ServiceCategory? = null,

		@field:SerializedName("received_whatsapp")
		val receivedWhatsapp: Boolean? = null,

		@field:SerializedName("is_available")
		@ColumnInfo(name = "is_available")
		val isAvailable: Boolean? = null

		) : Serializable