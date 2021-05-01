package com.raantech.solalat.provider.ui.medical

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityMediaBinding
import com.raantech.solalat.provider.databinding.ActivityMedicalServicesBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.medical.viewmodels.MedicalServicesViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicalServicesActivity : BaseBindingActivity<ActivityMedicalServicesBinding>() {

    private val viewModel: MedicalServicesViewModel by viewModels()

    companion object {
        fun start(
            context: Context?,
            addNew: Boolean? = false
        ) {
            val intent = Intent(context, MedicalServicesActivity::class.java)
            intent.putExtra(Constants.BundleData.ADD_NEW, addNew)
            context?.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addNew = intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)
        setContentView(R.layout.activity_medical_services, hasToolbar = false)
    }

}