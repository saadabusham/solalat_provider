package com.raantech.solalat.provider.ui.products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityProductsBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.auth_nav_host_fragment
import kotlinx.android.synthetic.main.activity_products.*

@AndroidEntryPoint
class ProductsActivity : BaseBindingActivity<ActivityProductsBinding>() {

    private val viewModel: ProductsViewModel by viewModels()

    companion object {
        fun start(
            context: Context?,
            addNew: Boolean? = false
        ) {
            val intent = Intent(context, ProductsActivity::class.java)
            intent.putExtra(Constants.BundleData.ADD_NEW, addNew)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addNew = intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)
        setContentView(R.layout.activity_products, hasToolbar = false)
        setStartDestination()
//        if (intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false))
//            try {
//                val navigationController =
//                    Navigation.findNavController(this, R.id.products_nav_host_fragment)
//                navigationController.navigate(R.id.action_productsFragment_to_addProductsFragment)
//            } catch (e: Exception) {
//
//            }
    }

    private fun setStartDestination() {
        val navHostFragment = products_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.products_nav_graph)

        if (intent.getBooleanExtra(Constants.BundleData.ADD_NEW, false)) {
            graph.startDestination = R.id.productsFragment
//            graph.startDestination = R.id.addProductsFragment
        } else {
            graph.startDestination = R.id.productsFragment
        }
        navHostFragment.navController.graph = graph
    }
}