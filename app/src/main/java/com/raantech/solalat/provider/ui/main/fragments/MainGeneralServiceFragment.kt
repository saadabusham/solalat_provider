package com.raantech.solalat.provider.ui.main.fragments

import androidx.fragment.app.activityViewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.databinding.FragmentMainGeneralServicesBinding
import com.raantech.solalat.provider.ui.add_new_service.AddNewServiceActivity
import com.raantech.solalat.provider.ui.barn.BarnActivity
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.medical.MedicalServicesActivity
import com.raantech.solalat.provider.ui.transportation.TransportationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainGeneralServiceFragment : BaseBindingFragment<FragmentMainGeneralServicesBinding>(){

    private val viewModel: MainViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_main_general_services

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
        binding?.btnAddService?.setOnClickListener {
            AddNewServiceActivity.start(requireContext())
        }
        binding?.btnEditInfo?.setOnClickListener {
            if (viewModel.currentService.value?.service == ServiceTypesEnum.MEDICAL.value) {
                MedicalServicesActivity.start(requireContext())
            } else if (viewModel.currentService.value?.service == ServiceTypesEnum.TRANSPORTATION.value) {
                TransportationActivity.start(requireContext())
            } else {
                BarnActivity.start(requireContext())
            }
        }
    }

}