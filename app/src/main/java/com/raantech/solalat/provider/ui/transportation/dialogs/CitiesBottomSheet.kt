package com.raantech.solalat.provider.ui.transportation.dialogs

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
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.databinding.BottomSheetCitiesBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.transportation.adapters.CityRecyclerAdapter
import com.raantech.solalat.provider.ui.transportation.viewmodels.TransportationViewModel
import com.raantech.solalat.provider.utils.extensions.longToast
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration

class CitiesBottomSheet(
        private val cityPickerCallBack: CityPickerCallBack,
        private val viewModel: TransportationViewModel,
        private val selectedList: MutableList<City>) :
        BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var buttomSheetCategoriesBinding: BottomSheetCitiesBinding
    lateinit var cityRecyclerAdapter: CityRecyclerAdapter

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
        buttomSheetCategoriesBinding =
                DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_cities, null, false)
        buttomSheetCategoriesBinding.viewModel = this
        isCancelable = true
        setUpRecyclerView()
        return buttomSheetCategoriesBinding.root
    }

    private fun setUpRecyclerView() {
        cityRecyclerAdapter = CityRecyclerAdapter(requireContext())
        buttomSheetCategoriesBinding.recyclerView.adapter = cityRecyclerAdapter
        buttomSheetCategoriesBinding.recyclerView.setOnItemClickListener(this)
        buttomSheetCategoriesBinding.recyclerView.addItemDecoration(
                VerticalSpaceDecoration(
                        resources.getDimension(R.dimen._5sdp).toInt(),
                        0
                )
        )
        viewModel.getCities().observe(this, citiesObserver())
    }

    private fun citiesObserver(): CustomObserverResponse<List<City>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<City>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<List<City>>?
                    ) {
                        data?.body?.let {
                            selectedList.forEach { selectedLanguage ->
                                it.forEach { city ->
                                    if (selectedLanguage.name == city.name) {
                                        city.selected = selectedLanguage.selected
                                    }
                                }
                            }
                            cityRecyclerAdapter.addItems(it)
                        }
                    }
                }
        )
    }



    fun onDoneClicked() {
        if (!cityRecyclerAdapter.items.filter { it.selected }.isNullOrEmpty()) {
            dismiss()
            val list = mutableListOf<City>()
            cityRecyclerAdapter.items.forEach {
                if (it.selected)
                    list.add(it)
            }
            cityPickerCallBack.callBack(list)
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

    interface CityPickerCallBack {
        fun callBack(citiesList: List<City>)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }
}