package com.raantech.solalat.provider.ui.add_new_service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.enums.ServiceTypesEnum
import com.raantech.solalat.provider.data.models.main.home.Service
import com.raantech.solalat.provider.databinding.ActivityAddNewServiceBinding
import com.raantech.solalat.provider.ui.add_new_service.viewmodels.AddNewServiceViewModel
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.main.adapters.ServicesRecyclerAdapter
import com.raantech.solalat.provider.ui.medical.MedicalServicesActivity
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.ui.transportation.TransportationActivity
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewServiceActivity : BaseBindingActivity<ActivityAddNewServiceBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: AddNewServiceViewModel by viewModels { defaultViewModelProviderFactory }

    lateinit var servicesCategoriesRecyclerAdapter: ServicesRecyclerAdapter

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
        setContentView(
            R.layout.activity_add_new_service,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false
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
        servicesCategoriesRecyclerAdapter = ServicesRecyclerAdapter(this)
        binding?.recyclerView?.adapter = servicesCategoriesRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        servicesCategoriesRecyclerAdapter.submitItems(
            arrayListOf(
                Service(
                    ServiceTypesEnum.ACCESSORIES,
                    resources.getString(R.string.add_accessories),
                    R.drawable.ic_cat_accessories
                ),

                Service(
                    ServiceTypesEnum.MEDICAL,
                    resources.getString(R.string.add_health_services),
                    R.drawable.ic_cat_medical
                ),

                Service(
                    ServiceTypesEnum.BARN,
                    resources.getString(R.string.add_barn),
                    R.drawable.ic_cat_barn
                ),

                Service(
                    ServiceTypesEnum.TRANSPORTATION,
                    resources.getString(R.string.add_transportation_services),
                    R.drawable.ic_cat_transportation
                )
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Service) {
            when (position) {
                ServiceTypesEnum.ACCESSORIES.ordinal -> ProductsActivity.start(this,true)
                ServiceTypesEnum.MEDICAL.ordinal -> MedicalServicesActivity.start(this,true)
                ServiceTypesEnum.BARN.ordinal -> ProductsActivity.start(this)
                ServiceTypesEnum.TRANSPORTATION.ordinal -> TransportationActivity.start(this)
            }
        }
    }


}