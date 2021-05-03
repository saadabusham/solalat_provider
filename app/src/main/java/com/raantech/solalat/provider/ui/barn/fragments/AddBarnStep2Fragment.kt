package com.raantech.solalat.provider.ui.barn.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.databinding.FragmentAddBarnStep2Binding
import com.raantech.solalat.provider.databinding.FragmentAddProductBinding
import com.raantech.solalat.provider.ui.barn.dialogs.ServicesBottomSheet
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.media.MediaActivity
import com.raantech.solalat.provider.ui.products.adapters.CategoriesSpinnerAdapter
import com.raantech.solalat.provider.ui.products.adapters.SmallMediaRecyclerAdapter
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import com.raantech.solalat.provider.ui.transportation.dialogs.CitiesBottomSheet
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class AddBarnStep2Fragment : BaseBindingFragment<FragmentAddBarnStep2Binding>() {

    private val viewModel: BarnViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_add_barn_step_2
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
            subTitle = R.string.add_new_barn
        )
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.tvSelectServices?.setOnClickListener {
            ServicesBottomSheet(object : ServicesBottomSheet.ServicesPickerCallBack {
                override fun callBack(selectedServices: List<ServiceCategory>) {
                    viewModel.services.clear()
                    viewModel.services.addAll(selectedServices)
                    binding?.tvSelectServices?.text = selectedServices.map { it.name }.joinToString()
                }
            },viewModel,viewModel.services).show(childFragmentManager, "CitiesPicker")
        }

        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValid()) {
                viewModel.addBarn(viewModel.getBarnRequest(
                    binding?.checkboxReceiveWhatsapp?.isChecked ?: false)
                ).observe(this, addProductResultObserver())
            }
        }
    }

    private fun isDataValid(): Boolean {
        binding?.edPrice?.text.toString().validate(
            ValidatorInputTypesEnums.DOUBLE,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = resources.getString(R.string.product_price),
                        message = it.errorMessage
                    )
                    return false
                }
            }

        binding?.edPhoneNumber?.text.toString().validate(
            ValidatorInputTypesEnums.PHONE_NUMBER,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = resources.getString(R.string.phone_number),
                        message = it.errorMessage
                    )
                    return false
                }
            }

        if (viewModel.services.size == 0) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.services),
                    resources.getString(R.string.please_select_the_provided_services)
            )
            return false
        }
        return true
    }

    private fun addProductResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    navigationController.navigate(R.id.action_addProductsFragment_to_productsFragment)
                }
            }, showError = true
        )
    }

}