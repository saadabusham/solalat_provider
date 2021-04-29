package com.raantech.solalat.provider.ui.dataview.dataviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.databinding.ActivityDataviewerBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class DataViewerActivity : BaseBindingActivity<ActivityDataviewerBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_dataviewer,
                hasToolbar = true,
                toolbarView = toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = intent.getStringExtra(Constants.title)

        )
        binding?.body = intent.getStringExtra(Constants.body)
    }

    companion object {

        fun start(context: Context?,
                  title: String,
                  body: String) {
            val intent = Intent(context, DataViewerActivity::class.java)
            intent.putExtra(Constants.title, title)
            intent.putExtra(Constants.body, body)
            context?.startActivity(intent)
        }

    }

}