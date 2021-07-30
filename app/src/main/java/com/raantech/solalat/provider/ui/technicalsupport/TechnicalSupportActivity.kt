package com.raantech.solalat.provider.ui.technicalsupport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.databinding.ActivityTechnicalSupportBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class TechnicalSupportActivity : BaseBindingActivity<ActivityTechnicalSupportBinding>() {

    private val viewModel: TechnicalSupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_technical_support,
                hasToolbar = true,
                toolbarView = toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                title = R.string.menu_technical_support
        )
        binding?.viewModel = viewModel
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding?.btnSend?.setOnClickListener {
            if (isDataValid()) {

            }
        }
    }

    private fun sendProblemResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
                this,
                object : CustomObserverResponse.APICallBack<Any> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: Any?
                    ) {

                    }
                })
    }


    private fun isDataValid(): Boolean {
        binding?.edTitle?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                this
        )
                .let {
                    if (!it.isValid) {
                        showValidationErrorAlert(
                                title = resources.getString(R.string.title_of_the_problem),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        binding?.edSummery?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                this
        )
                .let {
                    if (!it.isValid) {
                        showValidationErrorAlert(
                                title = resources.getString(R.string.complaint_summary),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        return true
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, TechnicalSupportActivity::class.java)
            context?.startActivity(intent)
        }

    }

}