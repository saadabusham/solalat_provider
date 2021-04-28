package com.raantech.solalat.provider.ui.add_new_service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.CategoriesTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Category
import com.raantech.solalat.provider.databinding.ActivityAddNewServiceBinding
import com.raantech.solalat.provider.ui.add_new_service.viewmodels.AddNewServiceViewModel
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.main.adapters.CategoriesRecyclerAdapter
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewServiceActivity : BaseBindingActivity<ActivityAddNewServiceBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: AddNewServiceViewModel by viewModels { defaultViewModelProviderFactory }

    lateinit var servicesCategoriesRecyclerAdapter: CategoriesRecyclerAdapter

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, AddNewServiceActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_service, hasToolbar = false)
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
        servicesCategoriesRecyclerAdapter = CategoriesRecyclerAdapter(this)
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
            when (position) {

            }
        }
    }


}