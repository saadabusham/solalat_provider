package com.raantech.solalat.provider.ui.main.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Service
import com.raantech.solalat.provider.databinding.FragmentMainMultipleServicesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.adapters.ServicesRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMultipleServicesFragment : BaseBindingFragment<FragmentMainMultipleServicesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var servicesCategoriesRecyclerAdapter: ServicesRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_main_multiple_services

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {
        setUpRecyclerView()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRecyclerView() {
        servicesCategoriesRecyclerAdapter = ServicesRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = servicesCategoriesRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        viewModel.myServices.forEach {
            if (it.service.equals(ServiceTypesEnum.ACCESSORIES.value)) {
                servicesCategoriesRecyclerAdapter.submitItem(
                    Service(
                        ServiceTypesEnum.ACCESSORIES,
                        resources.getString(R.string.add_accessories),
                        R.drawable.ic_cat_accessories,
                        it
                    )
                )
            } else if (it.service.equals(ServiceTypesEnum.MEDICAL.value)) {
                servicesCategoriesRecyclerAdapter.submitItem(
                    Service(
                        ServiceTypesEnum.MEDICAL,
                        resources.getString(R.string.add_health_services),
                        R.drawable.ic_cat_medical,
                        it
                    )
                )
            } else if (it.service.equals(ServiceTypesEnum.BARN.value)) {
                servicesCategoriesRecyclerAdapter.submitItem(
                    Service(
                        ServiceTypesEnum.BARN,
                        resources.getString(R.string.add_barn),
                        R.drawable.ic_cat_barn,
                        it
                    )
                )
            }
            else if (it.service.equals(ServiceTypesEnum.TRANSPORTATION.value)) {
                servicesCategoriesRecyclerAdapter.submitItem(
                    Service(
                        ServiceTypesEnum.TRANSPORTATION,
                        resources.getString(R.string.add_transportation_services),
                        R.drawable.ic_cat_transportation,
                        it
                    )
                )
            }
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Service) {
            viewModel.currentService.postValue(item.myService)
            when (position) {
                ServiceTypesEnum.ACCESSORIES.ordinal -> navigationController.navigate(R.id.action_mainMultipleServicesFragment_to_mainAccessoriesFragment)
                ServiceTypesEnum.MEDICAL.ordinal -> navigationController.navigate(R.id.action_mainMultipleServicesFragment_to_mainGeneralServiceFragment)
                ServiceTypesEnum.BARN.ordinal -> navigationController.navigate(R.id.action_mainMultipleServicesFragment_to_mainGeneralServiceFragment)
                ServiceTypesEnum.TRANSPORTATION.ordinal -> navigationController.navigate(R.id.action_mainMultipleServicesFragment_to_mainGeneralServiceFragment)
            }
        }
    }


}