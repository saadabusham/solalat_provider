package com.raantech.solalat.provider.ui.barn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityBarnBinding
import com.raantech.solalat.provider.databinding.ActivityProductsBinding
import com.raantech.solalat.provider.databinding.ActivityTransportationBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.auth_nav_host_fragment
import kotlinx.android.synthetic.main.activity_barn.*
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.activity_transportation.*

@AndroidEntryPoint
class BarnActivity : BaseBindingActivity<ActivityBarnBinding>() {

    private val viewModel: ProductsViewModel by viewModels()

    companion object {
        fun start(
            context: Context?,
            addNew: Boolean? = false
        ) {
            val intent = Intent(context, BarnActivity::class.java)
            intent.putExtra(Constants.BundleData.ADD_NEW, addNew)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addNew = intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)
        setContentView(R.layout.activity_barn, hasToolbar = false)
        setStartDestination()
    }


    private fun setStartDestination() {
        val navHostFragment = barn_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.barn_nav_graph)

        if (intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)) {
            graph.startDestination = R.id.addBarnStep1Fragment
        } else {
            graph.startDestination = R.id.barnsFragment
        }
        navHostFragment.navController.graph = graph
    }
}