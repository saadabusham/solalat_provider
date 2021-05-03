package com.raantech.solalat.provider.ui.barn.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.databinding.BottomSheetServicesBinding
import com.raantech.solalat.provider.ui.barn.adapters.SelectServicesRecyclerAdapter
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.utils.extensions.longToast
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration

class ServicesBottomSheet(
        private val servicesPickerCallBack: ServicesPickerCallBack,
        private val viewModel: BarnViewModel,
        private val selectedList: MutableList<ServiceCategory>) :
        BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var bottomSheetServicesBinding: BottomSheetServicesBinding
    lateinit var selectServicesRecyclerAdapter: SelectServicesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onStart() {
        super.onStart()
        view?.post {
            val parent = requireView().parent as View
            val params = (parent).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior
            bottomSheetBehavior.peekHeight = requireView().measuredHeight
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bottomSheetServicesBinding =
                DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_services, null, false)
        bottomSheetServicesBinding.viewModel = this
        isCancelable = true
        setUpRecyclerView()
        return bottomSheetServicesBinding.root
    }

    private fun setUpRecyclerView() {
        selectServicesRecyclerAdapter = SelectServicesRecyclerAdapter(requireContext())
        bottomSheetServicesBinding.recyclerView.adapter = selectServicesRecyclerAdapter
        bottomSheetServicesBinding.recyclerView.setOnItemClickListener(this)
        bottomSheetServicesBinding.recyclerView.addItemDecoration(
                VerticalSpaceDecoration(
                        resources.getDimension(R.dimen._5sdp).toInt(),
                        0
                )
        )
        viewModel.getServicesCategories().observe(this, servicesObserver())
    }

    private fun servicesObserver(): CustomObserverResponse<ServiceCategoriesResponse> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<ServiceCategoriesResponse> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<ServiceCategoriesResponse>?
                    ) {
                        data?.body?.categories?.let {
                            selectedList.forEach { selectedLanguage ->
                                it.forEach { city ->
                                    if (selectedLanguage.name == city.name) {
                                        city.selected = selectedLanguage.selected
                                    }
                                }
                            }
                            selectServicesRecyclerAdapter.addItems(it)
                        }
                    }
                }
        )
    }


    fun onDoneClicked() {
        if (!selectServicesRecyclerAdapter.items.filter { it.selected }.isNullOrEmpty()) {
            dismiss()
            val list = mutableListOf<ServiceCategory>()
            selectServicesRecyclerAdapter.items.forEach {
                if (it.selected)
                    list.add(it)
            }
            servicesPickerCallBack.callBack(list)
        } else {
            longToast(resources.getString(R.string.please_select_city))
        }
    }

    fun onCancelClicked() {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    interface ServicesPickerCallBack {
        fun callBack(citiesList: List<ServiceCategory>)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }
}