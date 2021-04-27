package com.raantech.solalat.provider.ui.auth.login.fragments

import androidx.navigation.navGraphViewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.GeneralError
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.auth.login.TokenModel
import com.raantech.solalat.provider.data.models.auth.login.UserDetailsResponseModel
import com.raantech.solalat.provider.data.pref.user.UserPref
import com.raantech.solalat.provider.databinding.FragmentVerificationLoginBinding
import com.raantech.solalat.provider.ui.auth.login.viewmodels.LoginViewModel
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.main.MainActivity
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_verification_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class VerificationLoginFragment : BaseBindingFragment<FragmentVerificationLoginBinding>() {

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.auth_nav_graph) { defaultViewModelProviderFactory }

    override fun getLayoutId(): Int = R.layout.fragment_verification_login
    @Inject
    lateinit var prefs: UserPref
    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.empty_string
        )
        setUpViewsListeners()
        setUpData()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.startHandleResendSignUpPinCodeTimer()
    }

    private fun verifyOtpResultObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(requireActivity(), object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
            override fun onSuccess(statusCode: Int, subErrorCode: ResponseSubErrorsCodeEnum, data: UserDetailsResponseModel?) {
                data?.let {
                    viewModel.storeUser(it)
                    MainActivity.start(requireContext())
                }
            }

            override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String, errors: List<GeneralError>?) {
                super.onError(subErrorCode, message, errors)
                errors?.get(0)?.let {
                    requireActivity().showErrorAlert(it.key,it.getErrorsString())
                }
            }
        })
    }

    private fun sendOtpResultObserver(): CustomObserverResponse<TokenModel> {
        return CustomObserverResponse(requireActivity(), object : CustomObserverResponse.APICallBack<TokenModel> {
            override fun onSuccess(statusCode: Int, subErrorCode: ResponseSubErrorsCodeEnum, data: TokenModel?) {
                viewModel.userTokenMutableLiveData.postValue(data?.token)
                viewModel.startHandleResendSignUpPinCodeTimer()
            }
            override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String, errors: List<GeneralError>?) {
                super.onError(subErrorCode, message, errors)
                errors?.get(0)?.let {
                    requireActivity().showErrorAlert(it.key,it.getErrorsString())
                }
            }
        })
    }

    private fun setUpViewsListeners() {
        otp_view.setAnimationEnable(true)
        binding?.tvTimeToResend?.setOnClickListener {
            if (viewModel.signUpResendPinCodeEnabled.value == true) {
            viewModel.resendVerificationCode().observe(this,sendOtpResultObserver())
            }
        }
        binding?.btnVerify?.setOnClickListener {
            if (validateInput()) {
                viewModel.verifyCode().observe(this,verifyOtpResultObserver())
            }
        }
    }

    private fun validateInput(): Boolean {
        otp_view.text.toString().validate(ValidatorInputTypesEnums.OTP, requireContext()).let {
            if (!it.isValid) {
                activity.showErrorAlert(it.errorTitle, it.errorMessage)
                return false
            }
        }
        return true
    }

}