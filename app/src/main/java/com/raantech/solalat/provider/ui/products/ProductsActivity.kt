package com.raantech.solalat.provider.ui.products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.pref.user.UserPref
import com.raantech.solalat.provider.databinding.ActivityAuthBinding
import com.raantech.solalat.provider.databinding.ActivityProductsBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductsActivity : BaseBindingActivity<ActivityProductsBinding>() {

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, ProductsActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products, hasToolbar = false)
    }

}