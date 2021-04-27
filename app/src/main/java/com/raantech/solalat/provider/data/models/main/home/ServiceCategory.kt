package com.raantech.solalat.provider.data.models.main.home

import com.raantech.solalat.provider.data.enums.ServicesCategoriesTypesEnum

data class ServiceCategory(
        val type: ServicesCategoriesTypesEnum,
        val title: String? = null,
        val icon: Int? = null)
