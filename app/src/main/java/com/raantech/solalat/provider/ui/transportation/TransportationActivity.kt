package com.raantech.solalat.provider.ui.transportation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityProductsBinding
import com.raantech.solalat.provider.databinding.ActivityTransportationBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.auth_nav_host_fragment
import kotlinx.android.synthetic.main.activity_medical_services.*
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.activity_transportation.*

@AndroidEntryPoint
class TransportationActivity : BaseBindingActivity<ActivityTransportationBinding>() {

    private val viewModel: ProductsViewModel by viewModels()

    companion object {
        fun start(
            context: Context?,
            addNew: Boolean? = false
        ) {
            val intent = Intent(context, TransportationActivity::class.java)
            intent.putExtra(Constants.BundleData.ADD_NEW, addNew)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addNew = intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)
        setContentView(R.layout.activity_transportation, hasToolbar = false)
        setStartDestination()
    }

    private fun setStartDestination() {
        val navHostFragment = transport_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.transportation_nav_graph)

        if (intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)) {
            graph.startDestination = R.id.addTransportationFragment
        } else {
            graph.startDestination = R.id.transportationFragment
        }
        navHostFragment.navController.graph = graph
    }
}