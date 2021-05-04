package com.raantech.solalat.provider.ui.medical.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.paginate.Paginate
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.GeneralError
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.databinding.FragmentMedicalServicesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.medical.adapters.MedicalsRecyclerAdapter
import com.raantech.solalat.provider.ui.medical.viewmodels.MedicalServicesViewModel
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.openDial
import com.raantech.solalat.provider.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class MedicalServicesFragment : BaseBindingFragment<FragmentMedicalServicesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MedicalServicesViewModel by activityViewModels()

    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    lateinit var medicalsRecyclerAdapter: MedicalsRecyclerAdapter

    var refreshData: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!refreshData) {
            refreshData = true
            return
        }
        medicalsRecyclerAdapter.clear()
        loadProducts()

    }

    override fun getLayoutId(): Int = R.layout.fragment_medical_services

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
            subTitle = R.string.view_medical_service
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
            navigationController.navigate(R.id.action_medicalServicesFragment_to_addMedicalServiceFragment)
        }
    }

    private fun loadProducts() {
        viewModel.getMedicals(medicalsRecyclerAdapter.itemCount).observe(this, medicalsObserver())
    }

    private fun setUpRecyclerView() {
        medicalsRecyclerAdapter = MedicalsRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = medicalsRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && medicalsRecyclerAdapter.itemCount > 0 && !isFinished) {
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
            .addLoadingListItem(false)
            .build()
    }

    private fun hideShowNoData() {
        if (medicalsRecyclerAdapter.itemCount == 0) {
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

    private fun medicalsObserver(): CustomObserverResponse<List<Medical>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<List<Medical>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<List<Medical>>?
                ) {
                    isFinished = data?.body?.isNullOrEmpty() == true
                    data?.body?.let {
                        medicalsRecyclerAdapter.addItems(it)
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
        item as Medical
        viewModel.medicalToUpdate = item
        navigationController.navigate(R.id.action_medicalServicesFragment_to_editMedicalServiceFragment)
    }


}