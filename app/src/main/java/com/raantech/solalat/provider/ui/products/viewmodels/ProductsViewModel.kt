package com.raantech.solalat.provider.ui.products.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.raantech.solalat.provider.data.enums.UserEnums
import com.raantech.solalat.provider.data.repos.auth.UserRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel

class ProductsViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        val userRepo: UserRepo
) : BaseViewModel() {
}