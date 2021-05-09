package com.raantech.solalat.provider.ui.barn.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.paginate.Paginate
import com.paginate.recycler.LoadingListItemCreator
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.GeneralError
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.barn.respnose.Barn
import com.raantech.solalat.provider.databinding.FragmentBarnBinding
import com.raantech.solalat.provider.ui.barn.adapters.BarnsRecyclerAdapter
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class BarnsFragment : BaseBindingFragment<FragmentBarnBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BarnViewModel by activityViewModels()

    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    lateinit var barnsRecyclerAdapter: BarnsRecyclerAdapter

    var refreshData: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!refreshData) {
            refreshData = true
            return
        }
        barnsRecyclerAdapter.clear()
        loadProducts()

    }

    override fun getLayoutId(): Int = R.layout.fragment_barn

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
                hasToolbar = true,
                hasBackButton = true,
                showBackArrow = true,
                toolbarView = toolbar,
                hasTitle = true,
                title = R.string.solalat_services,
                hasSubTitle = true,
                subTitle = R.string.view_barns
        )
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {
        setUpRecyclerView()
        loadProducts()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            navigationController.navigate(R.id.action_barnsFragment_to_addBarnStep1Fragment)
        }
    }

    private fun loadProducts() {
        viewModel.getBarns(barnsRecyclerAdapter.itemCount).observe(this, barnsObserver())
    }

    private fun setUpRecyclerView() {
        barnsRecyclerAdapter = BarnsRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = barnsRecyclerAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && barnsRecyclerAdapter.itemCount > 0 && !isFinished) {
                    loadProducts()
                }
            }

            override fun isLoading(): Boolean {
                return loading.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(object : LoadingListItemCreator {
                    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
                        val view = LayoutInflater.from(parent!!.context)
                                .inflate(R.layout.loading_row, parent, false)
                        return object : RecyclerView.ViewHolder(view) {}
                    }

                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

                    }

                })
                .build()
    }

    private fun hideShowNoData() {
        if (barnsRecyclerAdapter.itemCount == 0) {
            binding?.recyclerView?.gone()
            binding?.layoutNoData?.linearNoData?.visible()
        } else {
            binding?.layoutNoData?.linearNoData?.gone()
            binding?.recyclerView?.visible()
        }
    }

    private fun observeLoading() {
        loading.observe(this, Observer {
            if (it) {
                binding?.recyclerView?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.visible()
//                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
//                binding?.layoutShimmer?.shimmerViewContainer?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.recyclerView?.visible()
            }
        })
    }

    private fun barnsObserver(): CustomObserverResponse<List<Barn>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Barn>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<List<Barn>>?
                    ) {
                        isFinished = data?.body?.isNullOrEmpty() == true
                        data?.body?.let {
                            barnsRecyclerAdapter.addItems(it)
                        }
                        loading.postValue(false)
                        hideShowNoData()
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loading.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loading.postValue(true)
                    }
                }, false, showError = false
        )
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Barn
        viewModel.barnToEdit = item
        navigationController.navigate(R.id.action_barnsFragment_to_barnDetailsFragment, bundleOf(Pair(Constants.BundleData.SERVICE,item)))
    }


}