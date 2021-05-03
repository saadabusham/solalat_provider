package com.raantech.solalat.provider.data.models.more

import com.google.gson.annotations.SerializedName

data class FaqsResponse(
        @field:SerializedName("id")
        var id: Int,
        @field:SerializedName("question")
        var question: String,
        @field:SerializedName("answer")
        var answer: String,
        var isExpanded: Boolean = false
)