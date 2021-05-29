package com.raantech.solalat.provider.ui.updateprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.databinding.ActivityUpdateProfileBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.map.MapActivity
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.getLocationName
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class UpdateProfileActivity : BaseBindingActivity<ActivityUpdateProfileBinding>() {

    private val viewModel: UpdateProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_update_profile,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            toolbarView = toolbar,
            hasTitle = true,
            title = R.string.profile,
            hasSubTitle = false
        )
        setUpBinding()
        setUpListeners()
    }
    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        viewModel.selectedCountryCode.postValue(binding?.countryCodePicker?.defaultCountryCode)
        binding?.countryCodePicker?.setOnCountryChangeListener {
            viewModel.selectedCountryCode.postValue(it.phoneCode)
        }
        binding?.btnSave?.setOnClickListener {
            if (isDataValid()) {
//                viewModel.addMedical(
//                    categoriesSpinnerAdapter.spinnerItems[categoriesSpinnerAdapter.index],
//                    binding?.checkboxReceiveWhatsapp?.isChecked ?: false,
//                    binding?.checkboxIsActive?.isChecked ?: false
//                ).observe(this, addMedicalResultObserver())
            }
        }
        binding?.tvLocation?.setOnClickListener {
            MapActivity.start(this, resultLauncher)
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
    private fun configurationResultObserver(): CustomObserverResponse<ConfigurationWrapperResponse> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<ConfigurationWrapperResponse> {
                override fun onSuccess(
                        statusCode: Int,
                        subErrorCode: ResponseSubErrorsCodeEnum,
                        data: ConfigurationWrapperResponse?
                ) {

                }
            })
    }


    private fun isDataValid(): Boolean {
        binding?.edName?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,this

        )
            .let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = resources.getString(R.string.name),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edEmail?.text.toString().validate(
            ValidatorInputTypesEnums.EMAIL,this

        )
            .let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = it.errorTitle,
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edPhoneNumber?.text.toString().validate(
            ValidatorInputTypesEnums.PHONE_NUMBER,this

        )
            .let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = resources.getString(R.string.phone_number),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        if (viewModel.address.value?.lat == null) {
            showErrorAlert(
                resources.getString(R.string.location),
                resources.getString(R.string.please_pick_location)
            )
            return false
        }
        return true
    }


    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, UpdateProfileActivity::class.java)
            context?.startActivity(intent)
        }

    }

}