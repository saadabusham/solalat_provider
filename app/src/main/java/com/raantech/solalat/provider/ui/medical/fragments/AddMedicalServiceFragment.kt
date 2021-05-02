package com.raantech.solalat.provider.ui.medical.fragments

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.databinding.FragmentAddMedicalServiceBinding
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.map.MapActivity
import com.raantech.solalat.provider.ui.medical.viewmodels.MedicalServicesViewModel
import com.raantech.solalat.provider.ui.products.adapters.CategoriesSpinnerAdapter
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.getLocationName
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class AddMedicalServiceFragment : BaseBindingFragment<FragmentAddMedicalServiceBinding>() {

    private val viewModel: MedicalServicesViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_add_medical_service
    private lateinit var categoriesSpinnerAdapter: CategoriesSpinnerAdapter
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
                subTitle = R.string.add_medical_service
        )
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {
        setUpCategories()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValid()) {
                viewModel.addMedical(
                        categoriesSpinnerAdapter.spinnerItems[categoriesSpinnerAdapter.index],
                        binding?.checkboxReceiveWhatsapp?.isChecked ?: false
                ).observe(this, addProductResultObserver())
            }
        }
        binding?.tvLocation?.setOnClickListener {
            MapActivity.start(requireActivity(), resultLauncher)
        }
    }

    var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    viewModel.address.value = data?.getSerializableExtra(Constants.BundleData.ADDRESS) as Address
                    binding?.tvLocation?.text =
                            getLocationName(
                                    viewModel.address.value?.lat,
                                    viewModel.address.value?.lon
                            ).also {
                                viewModel.addressString.postValue(it)
                            }
                }
            }

    private fun isDataValid(): Boolean {
        if (categoriesSpinnerAdapter.index == -1) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.please_select_the_category)
            )
            return false
        }
        binding?.edName?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                requireContext()
        )
                .let {
                    if (!it.isValid) {
                        requireActivity().showValidationErrorAlert(
                                title = resources.getString(R.string.name),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        binding?.edExperiences?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                requireContext()
        )
                .let {
                    if (!it.isValid) {
                        requireActivity().showValidationErrorAlert(
                                title = resources.getString(R.string.experiences),
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
        if (viewModel.address.value?.lat == null) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.location),
                    resources.getString(R.string.please_pick_location)
            )
            return false
        }
        return true
    }

    private fun setUpCategories() {
        categoriesSpinnerAdapter =
                CategoriesSpinnerAdapter(binding!!.spinnerCategory, requireContext())
        categoriesSpinnerAdapter.let { binding?.spinnerCategory?.setSpinnerAdapter(it) }
        binding?.spinnerCategory?.getSpinnerRecyclerView()?.layoutManager =
                LinearLayoutManager(activity)
        binding?.spinnerCategory?.setOnSpinnerItemSelectedListener<ServiceCategory> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerCategory?.dismiss()
        }
        viewModel.getAccessoriesCategories()
                .observe(this, categoriesResultObserver())
    }

    private fun categoriesResultObserver(): CustomObserverResponse<ServiceCategoriesResponse> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<ServiceCategoriesResponse> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ServiceCategoriesResponse?
                    ) {
                        data?.categories?.let {
                            categoriesSpinnerAdapter.setItems(it)
                            binding?.spinnerCategory?.selectItemByIndex(0)
                        }
                    }
                })
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
                        navigationController.navigate(R.id.action_addMedicalServiceFragment_to_medicalServicesFragment)
                    }
                }, showError = true
        )
    }


}