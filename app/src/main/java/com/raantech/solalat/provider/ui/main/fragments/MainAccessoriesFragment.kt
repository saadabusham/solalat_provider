package com.raantech.solalat.provider.ui.main.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.product.response.product.Product
import com.raantech.solalat.provider.databinding.FragmentMainAccessoriesBinding
import com.raantech.solalat.provider.ui.add_new_service.AddNewServiceActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.adapters.ProductsVerticalGridRecyclerAdapter
import com.raantech.solalat.provider.ui.main.viewmodels.MainViewModel
import com.raantech.solalat.provider.ui.products.ProductsActivity
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAccessoriesFragment : BaseBindingFragment<FragmentMainAccessoriesBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    lateinit var productsRecyclerAdapter: ProductsVerticalGridRecyclerAdapter

    var refreshData: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!refreshData) {
            refreshData = true
            return
        }
        productsRecyclerAdapter.clear()
        loadProducts()

    }

    override fun getLayoutId(): Int = R.layout.fragment_main_accessories

    override fun onViewVisible() {
        super.onViewVisible()
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
            ProductsActivity.start(requireContext(), true)
        }
        binding?.btnAddService?.setOnClickListener {
            AddNewServiceActivity.start(requireContext())
        }
        binding?.btnEditInfo?.setOnClickListener {
            ProductsActivity.start(requireContext(), false)
        }
    }

    private fun loadProducts() {
        viewModel.getProducts(productsRecyclerAdapter.itemCount).observe(this, productsObserver())
    }

    private fun setUpRecyclerView() {
        productsRecyclerAdapter = ProductsVerticalGridRecyclerAdapter(requireContext())
        binding?.rvProducts?.adapter = productsRecyclerAdapter
        binding?.rvProducts?.setOnItemClickListener(this)
        Paginate.with(binding?.rvProducts, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && productsRecyclerAdapter.itemCount > 0 && !isFinished) {
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
        if (productsRecyclerAdapter.itemCount == 0) {
            binding?.rvProducts?.gone()
//            binding?.layoutNoData?.linearNoData?.visible()
        } else {
//            binding?.layoutNoData?.linearNoData?.gone()
            binding?.rvProducts?.visible()
        }
    }

    private fun observeLoading() {
        loading.observe(this, Observer {
            if (it) {
                binding?.rvProducts?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.visible()
//                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
//                binding?.layoutShimmer?.shimmerViewContainer?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.rvProducts?.visible()
            }
        })
    }

    private fun productsObserver(): CustomObserverResponse<List<Product>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Product>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<List<Product>>?
                    ) {
                        isFinished = data?.body?.isNullOrEmpty() == true
                        data?.body?.let {
                            productsRecyclerAdapter.addItems(it)
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

    }


}