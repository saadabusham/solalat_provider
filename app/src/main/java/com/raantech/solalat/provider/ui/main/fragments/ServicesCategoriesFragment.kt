package com.raantech.solalat.provider.ui.main.fragments

import android.view.View
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServicesCategoriesTypesEnum
import com.raantech.solalat.provider.data.models.main.home.ServiceCategory
import com.raantech.solalat.provider.databinding.FragmentServicesCategoriesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.adapters.ServicesCategoriesRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.utils.extensions.longToast
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesCategoriesFragment : BaseBindingFragment<FragmentServicesCategoriesBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels()

    lateinit var servicesCategoriesRecyclerAdapter: ServicesCategoriesRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_services_categories

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
        servicesCategoriesRecyclerAdapter = ServicesCategoriesRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = servicesCategoriesRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
                VerticalSpaceDecoration(
                        0, resources.getDimension(R.dimen._10sdp).toInt()
                )
        )
        servicesCategoriesRecyclerAdapter.submitItems(
                arrayListOf(
                        ServiceCategory(ServicesCategoriesTypesEnum.ACCESSORIES,
                                resources.getString(R.string.add_accessories),
                                R.drawable.ic_cat_medical),

                        ServiceCategory(ServicesCategoriesTypesEnum.MEDICAL,
                                resources.getString(R.string.add_health_services),
                                R.drawable.ic_cat_medical),

                        ServiceCategory(ServicesCategoriesTypesEnum.STABLE,
                                resources.getString(R.string.add_stables),
                                R.drawable.ic_cat_medical),

                        ServiceCategory(ServicesCategoriesTypesEnum.TRANSPORTATION,
                                resources.getString(R.string.add_transportation_services),
                                R.drawable.ic_cat_medical)))
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is ServiceCategory)
            longToast(item.title)
    }


}