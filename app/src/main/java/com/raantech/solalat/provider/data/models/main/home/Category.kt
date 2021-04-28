package com.raantech.solalat.provider.data.models.main.home

import com.raantech.solalat.provider.data.enums.CategoriesTypesEnum

data class Category(
        val type: CategoriesTypesEnum,
        val title: String? = null,
        val icon: Int? = null)
