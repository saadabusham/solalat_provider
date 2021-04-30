package com.raantech.solalat.provider.data.models.more

data class FaqsResponse(
        var id: Int,
        var question: String,
        var answer: String,

        @Transient
        var isExpanded: Boolean = false
)