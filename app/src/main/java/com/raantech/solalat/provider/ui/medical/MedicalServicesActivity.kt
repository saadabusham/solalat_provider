package com.raantech.solalat.provider.ui.medical

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityMediaBinding
import com.raantech.solalat.provider.databinding.ActivityMedicalServicesBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.medical.viewmodels.MedicalServicesViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_medical_services.*
import kotlinx.android.synthetic.main.activity_products.*

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
        setStartDestination()
    }
    private fun setStartDestination() {
        val navHostFragment = medical_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.medical_nav_graph)

        if (intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)) {
            graph.startDestination = R.id.addMedicalServiceFragment
        } else {
            graph.startDestination = R.id.medicalServicesFragment
        }
        navHostFragment.navController.graph = graph
    }
}