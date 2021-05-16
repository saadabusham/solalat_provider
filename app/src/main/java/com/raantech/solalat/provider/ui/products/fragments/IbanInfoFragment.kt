package com.raantech.solalat.provider.ui.products.fragments

import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.databinding.FragmentAccessoryAddedSuccessBinding
import com.raantech.solalat.provider.databinding.FragmentIbanInfoBinding
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IbanInfoFragment :
        BaseBindingFragment<FragmentIbanInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_iban_info

    override fun onViewVisible() {
        super.onViewVisible()
        setUpViewsListeners()
    }

    private fun setUpViewsListeners() {
        binding?.btnDone?.setOnClickListener {
            navigationController.navigateUp()
        }
    }
}