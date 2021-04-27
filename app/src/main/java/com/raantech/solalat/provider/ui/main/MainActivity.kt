package com.raantech.solalat.provider.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.fragment.NavHostFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.models.main.home.ServiceCategory
import com.raantech.solalat.provider.databinding.ActivityMainBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.main.adapters.DrawerRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.utils.LocaleUtil
import com.raantech.solalat.provider.utils.extensions.longToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels { defaultViewModelProviderFactory }
    lateinit var drawerRecyclerAdapter: DrawerRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_main,
                hasToolbar = true,
                hasTitle = true,
                title = R.string.solalat_services
        )
        setUpBinding()
        setStartDestination()
        setUpDrawer()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpDrawer() {
        drawerRecyclerAdapter = DrawerRecyclerAdapter(this)
        drawerRecyclerAdapter.submitItems(getDrawerList())
        binding?.drawerRecyclerView?.adapter = drawerRecyclerAdapter
        binding?.drawerRecyclerView?.setOnItemClickListener(this)
        val toggle = ActionBarDrawerToggle(
                this, binding?.drawerLayout, binding?.appBarMain?.layoutToolbar?.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        initDrawer(toggle)
    }


    private fun initDrawer(toggle: ActionBarDrawerToggle) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_menu,
                theme)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(drawable)
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener { v: View? ->
            if (binding?.drawerLayout?.isDrawerVisible(GravityCompat.START) == true) {
                binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }

        binding?.drawerLayout?.setScrimColor(Color.TRANSPARENT)
        binding?.drawerLayout?.drawerElevation = 0.toFloat()
//        binding?.drawerLayout?.setScrimColor(resources.getColor(R.color.button_color))
        binding?.drawerLayout?.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawer: View, slideOffset: Float) {
                if (LocaleUtil.getLanguage() == "ar") {
                    binding?.appBarMain?.holder?.rotation = (slideOffset * -1) * 10
                    binding?.appBarMain?.container?.scaleX = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.scaleY = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.pivotX = 0.toFloat()
                    binding?.appBarMain?.container?.pivotY = (1000).toFloat()
                } else {
                    binding?.appBarMain?.container?.x = (binding?.navigationView?.width!! * (slideOffset))
                    binding?.appBarMain?.holder?.rotation = slideOffset * 10
                    binding?.appBarMain?.container?.scaleX = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.scaleY = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.pivotX = 0.toFloat()
                    binding?.appBarMain?.container?.pivotY = (1000).toFloat()
                }
            }

            override fun onDrawerClosed(drawerView: View) {}
        }
        )
    }

    private fun getDrawerList(): List<String> {
        return arrayListOf(
                resources.getString(R.string.menu_my_account),
                resources.getString(R.string.menu_notifications),
                resources.getString(R.string.menu_report_user),
                resources.getString(R.string.menu_technical_support),
                resources.getString(R.string.menu_about_us))
    }

    private fun setStartDestination() {
        val navHostFragment = main_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_nav_graph)

        if (true) {
            graph.startDestination = R.id.servicesCategoriesFragment
        } else {
            graph.startDestination = R.id.loginFragment
        }
        navHostFragment.navController.graph = graph
    }

    companion object {
        const val EXTRA_FROM_NOTIFICATION = "notifications"
        fun start(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is ServiceCategory)
            longToast(item.title)
        else if(item is String) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            longToast(item)
        }
    }
}