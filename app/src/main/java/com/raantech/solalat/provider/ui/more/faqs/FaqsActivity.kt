package com.raantech.solalat.provider.ui.more.faqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.more.FaqsResponse
import com.raantech.solalat.provider.databinding.ActivityFaqsBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.more.faqs.adapter.FaqsRecyclerAdapter
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.openDial
import com.raantech.solalat.provider.utils.extensions.visible
import com.raantech.solalat.provider.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FaqsActivity : BaseBindingActivity<ActivityFaqsBinding>() {

    private val viewModel: FaqsViewModel by viewModels()
    lateinit var adapter: FaqsRecyclerAdapter
    private var list = ArrayList<FaqsResponse>()
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_faqs,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            titleString = resources.getString(R.string.help_request)
        )
        init()
    }

    private fun init() {
        setUpListeners()
        setUpAdapter()
        initSearch()
        viewModel.getFAQs().observe(this, faqsObserver())
    }

    private fun faqsObserver(): CustomObserverResponse<List<FaqsResponse>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<FaqsResponse>> {

                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<FaqsResponse>?
                ) {
                    if (data.isNullOrEmpty()) {
                        binding?.tvNoData?.visible()
                        return
                    }
                    list = data as ArrayList<FaqsResponse>
                    adapter.submitItems(list)
                }
            })
    }


    private fun setUpAdapter() {
        adapter = FaqsRecyclerAdapter(this)
        binding?.rvHelpCenter?.adapter = adapter
    }

    private fun setUpListeners() {
        binding?.btnCallUsNow?.setOnClickListener {
            openDial(viewModel.getPhoneNumber())
        }
    }

    private fun initSearch() {
        if (binding?.etSearch?.text?.isEmpty() == true) {
            adapter.submitItems(list)
        }
        disposable + binding?.etSearch?.textChangeEvents()
            ?.debounce(300, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    adapter.clear()
                    list.forEach { symptom ->
                        if (symptom.question.contains(it.text)) {
                            adapter.submitItem(symptom)
                        }
                    }
                    if (adapter.itemCount == 0) {
                        binding?.tvNoData?.visible()
                    } else {
                        binding?.tvNoData?.gone()
                    }
                } else {
                    binding?.tvNoData?.gone()
                    adapter.submitItems(list)
                }
            }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, FaqsActivity::class.java)
            context?.startActivity(intent)
        }

    }

}