package com.raantech.solalat.provider.ui.barn.fragments

import androidx.fragment.app.activityViewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.barn.respnose.ServicesItem
import com.raantech.solalat.provider.databinding.FragmentAddBarnStep2Binding
import com.raantech.solalat.provider.ui.barn.dialogs.ServicesBottomSheet
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.utils.extensions.checkPhoneNumberFormat
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class EditBarnStep2Fragment : BaseBindingFragment<FragmentAddBarnStep2Binding>() {

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
                subTitle = R.string.edit_barn
        )
        setUpBinding()
        setUpListeners()
        setUpData()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }


    private fun setUpData() {
        viewModel.barnToEdit?.let {
            viewModel.services.clear()
            it.services?.let { it1 -> viewModel.services.addAll(it1) }
            binding?.tvSelectServices?.text = viewModel.services.map { it.name }.joinToString()
            viewModel.price.postValue(it.price?.amount)
            viewModel.phoneNumber.postValue(it.contactNumber?.checkPhoneNumberFormat())
            binding?.checkboxReceiveWhatsapp?.isChecked = it.receivedWhatsapp ?: false
        }
    }


    private fun setUpListeners() {
        binding?.btnAddBarn?.text = resources.getString(R.string.save_changes)
        viewModel.selectedCountryCode.postValue(binding?.countryCodePicker?.defaultCountryCode)
        binding?.countryCodePicker?.setOnCountryChangeListener {
            viewModel.selectedCountryCode.postValue(it.phoneCode)
        }
        binding?.tvSelectServices?.setOnClickListener {
            ServicesBottomSheet(object : ServicesBottomSheet.ServicesPickerCallBack {
                override fun callBack(selectedServices: List<ServicesItem>) {
                    viewModel.services.clear()
                    viewModel.services.addAll(selectedServices)
                    binding?.tvSelectServices?.text = selectedServices.map { it.name }.joinToString()
                }
            }, viewModel, viewModel.services).show(childFragmentManager, "CitiesPicker")
        }

        binding?.btnAddBarn?.setOnClickListener {
            if (isDataValid()) {
                viewModel.updatedBarn(viewModel.getBarnRequest(
                        binding?.checkboxReceiveWhatsapp?.isChecked ?: false)
                ).observe(this, updateResultObserver())
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

    private fun updateResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<Any> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: Any?
                    ) {
                    navigationController.navigate(R.id.action_editBarnStep2Fragment_to_barnsFragment)
                    }
                }, showError = true
        )
    }

}