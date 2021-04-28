package com.raantech.solalat.provider.ui.main.fragments

import android.view.View
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.CategoriesTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Category
import com.raantech.solalat.provider.databinding.FragmentMainCategoriesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.adapters.CategoriesRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCategoriesFragment : BaseBindingFragment<FragmentMainCategoriesBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels()

    lateinit var servicesCategoriesRecyclerAdapter: CategoriesRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_main_categories

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
        servicesCategoriesRecyclerAdapter = CategoriesRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = servicesCategoriesRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
                VerticalSpaceDecoration(
                        0, resources.getDimension(R.dimen._10sdp).toInt()
                )
        )
        servicesCategoriesRecyclerAdapter.submitItems(
                arrayListOf(
                        Category(CategoriesTypesEnum.ACCESSORIES,
                                resources.getString(R.string.add_accessories),
                                R.drawable.ic_cat_accessories),

                        Category(CategoriesTypesEnum.MEDICAL,
                                resources.getString(R.string.add_health_services),
                                R.drawable.ic_cat_medical),

                        Category(CategoriesTypesEnum.BARN,
                                resources.getString(R.string.add_barn),
                                R.drawable.ic_cat_barn),

                        Category(CategoriesTypesEnum.TRANSPORTATION,
                                resources.getString(R.string.add_transportation_services),
                                R.drawable.ic_cat_transportation)))
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Category) {
            when(position){
                CategoriesTypesEnum.ACCESSORIES.ordinal -> ProductsActivity.start(requireContext())
                CategoriesTypesEnum.MEDICAL.ordinal -> ProductsActivity.start(requireContext())
                CategoriesTypesEnum.BARN.ordinal -> ProductsActivity.start(requireContext())
                CategoriesTypesEnum.TRANSPORTATION.ordinal -> ProductsActivity.start(requireContext())
            }
        }
    }


}