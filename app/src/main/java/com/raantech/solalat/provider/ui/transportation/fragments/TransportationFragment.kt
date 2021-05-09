package com.raantech.solalat.provider.ui.transportation.fragments

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.paginate.Paginate
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.GeneralError
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import com.raantech.solalat.provider.databinding.FragmentTransportationBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.transportation.adapters.TransportationRecyclerAdapter
import com.raantech.solalat.provider.ui.transportation.viewmodels.TransportationViewModel
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class TransportationFragment : BaseBindingFragment<FragmentTransportationBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: TransportationViewModel by activityViewModels()

    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    lateinit var transportationRecyclerAdapter: TransportationRecyclerAdapter

    var refreshData: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!refreshData) {
            refreshData = true
            return
        }
        transportationRecyclerAdapter.clear()
        loadProducts()

    }

    override fun getLayoutId(): Int = R.layout.fragment_transportation

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
            subTitle = R.string.view_transport_service
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
            navigationController.navigate(R.id.action_transportationFragment_to_addTransportationFragment)
        }
    }

    private fun loadProducts() {
        viewModel.getTransportation(transportationRecyclerAdapter.itemCount).observe(this, transportationObserver())
    }

    private fun setUpRecyclerView() {
        transportationRecyclerAdapter = TransportationRecyclerAdapter(requireContext(),object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && transportationRecyclerAdapter.itemCount > 0 && !isFinished) {
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
        binding?.recyclerView?.adapter = transportationRecyclerAdapter
        binding?.recyclerView.setOnItemClickListener(this)
    }

    private fun hideShowNoData() {
        if (transportationRecyclerAdapter.itemCount == 0) {
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

    private fun transportationObserver(): CustomObserverResponse<List<Transportation>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<List<Transportation>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<List<Transportation>>?
                ) {
                    isFinished = data?.body?.isNullOrEmpty() == true
                    data?.body?.let {
                        transportationRecyclerAdapter.addItems(it)
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
            }, true,showError = false
        )
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Transportation
        viewModel.transpornToEdit = item
        navigationController.navigate(R.id.action_transportationFragment_to_editTransportationFragment, bundleOf(Pair(Constants.BundleData.SERVICE,item)))
    }


}