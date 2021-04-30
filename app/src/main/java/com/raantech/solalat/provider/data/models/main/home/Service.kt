package com.raantech.solalat.provider.data.models.main.home

import com.raantech.solalat.provider.data.enums.ServiceTypesEnum

data class Service(
        val type: ServiceTypesEnum,
        val title: String? = null,
        val icon: Int? = null,
        val myService: MyService? = null
)
