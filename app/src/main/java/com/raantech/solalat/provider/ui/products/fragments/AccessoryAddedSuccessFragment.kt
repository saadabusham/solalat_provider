package com.raantech.solalat.provider.ui.products.fragments

import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.databinding.FragmentAccessoryAddedSuccessBinding
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessoryAddedSuccessFragment :
        BaseBindingFragment<FragmentAccessoryAddedSuccessBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_accessory_added_success

    override fun onViewVisible() {
        super.onViewVisible()
        setUpViewsListeners()
    }

    private fun setUpViewsListeners() {
        binding?.btnDone?.setOnClickListener {
            navigationController.navigate(R.id.action_accessoryAddedSuccessFragment_to_productsFragment)
        }
    }
}