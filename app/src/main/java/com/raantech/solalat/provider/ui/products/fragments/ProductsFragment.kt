package com.raantech.solalat.provider.ui.products.fragments

import android.view.View
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.models.main.home.accessories.Product
import com.raantech.solalat.provider.databinding.FragmentProductsBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.products.adapters.ProductsRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.AccessoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class ProductsFragment : BaseBindingFragment<FragmentProductsBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: AccessoriesViewModel by viewModels()

    lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_products

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
                hasToolbar = true,
                hasBackButton = true,
                showBackArrow = true,
                toolbarView = toolbar,
                hasTitle = true,
                title = R.string.solalat_services,
                hasSubTitle = true,
                subTitle = R.string.view_products
        )
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
        productsRecyclerAdapter = ProductsRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = productsRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        productsRecyclerAdapter.submitItems(
                arrayListOf(
                        Product(),Product(),Product(),Product(),Product()))
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }


}