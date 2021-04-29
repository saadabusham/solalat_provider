package com.raantech.solalat.provider.ui.main.fragments

import android.view.View
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Service
import com.raantech.solalat.provider.databinding.FragmentMainAccessoriesBinding
import com.raantech.solalat.provider.ui.add_new_service.AddNewServiceActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.viewmodels.AccessoriesViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAccessoriesFragment : BaseBindingFragment<FragmentMainAccessoriesBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: AccessoriesViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_main_accessories

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {

    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            ProductsActivity.start(requireContext())
        }
        binding?.btnAddService?.setOnClickListener {
            AddNewServiceActivity.start(requireContext())
        }
        binding?.btnEditInfo?.setOnClickListener {

        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Service) {
            when (position) {
                ServiceTypesEnum.ACCESSORIES.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.MEDICAL.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.BARN.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.TRANSPORTATION.ordinal -> ProductsActivity.start(requireContext())
            }
        }
    }


}