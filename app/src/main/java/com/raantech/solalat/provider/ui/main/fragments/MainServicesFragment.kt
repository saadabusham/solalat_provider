package com.raantech.solalat.provider.ui.main.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Service
import com.raantech.solalat.provider.databinding.FragmentMainServicesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.adapters.ServicesRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainServicesFragment : BaseBindingFragment<FragmentMainServicesBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var servicesCategoriesRecyclerAdapter: ServicesRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_main_services

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
        servicesCategoriesRecyclerAdapter.submitItems(
                arrayListOf(
                        Service(ServiceTypesEnum.ACCESSORIES,
                                resources.getString(R.string.add_accessories),
                                R.drawable.ic_cat_accessories),

                        Service(ServiceTypesEnum.MEDICAL,
                                resources.getString(R.string.add_health_services),
                                R.drawable.ic_cat_medical),

                        Service(ServiceTypesEnum.BARN,
                                resources.getString(R.string.add_barn),
                                R.drawable.ic_cat_barn),

                        Service(ServiceTypesEnum.TRANSPORTATION,
                                resources.getString(R.string.add_transportation_services),
                                R.drawable.ic_cat_transportation)))
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Service) {
            when(position){
                ServiceTypesEnum.ACCESSORIES.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.MEDICAL.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.BARN.ordinal -> ProductsActivity.start(requireContext())
                ServiceTypesEnum.TRANSPORTATION.ordinal -> ProductsActivity.start(requireContext())
            }
        }
    }


}