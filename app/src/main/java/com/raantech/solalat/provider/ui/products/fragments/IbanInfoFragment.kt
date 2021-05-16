package com.raantech.solalat.provider.ui.products.fragments

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.product.response.Bank
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.databinding.FragmentIbanInfoBinding
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.products.adapters.BankSpinnerAdapter
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class IbanInfoFragment :
        BaseBindingFragment<FragmentIbanInfoBinding>() {

    private val viewModel: ProductsViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_iban_info

    private lateinit var bankSpinnerAdapter: BankSpinnerAdapter

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
                hasToolbar = true,
                hasBackButton = true,
                showBackArrow = true,
                toolbarView = toolbar,
                hasTitle = true,
                title = R.string.add_accessories,
                hasSubTitle = true,
                subTitle = R.string.bank_account_information
        )
        binding?.viewModel = viewModel
        setUpViewsListeners()
        setUpBanks()
    }

    private fun setUpViewsListeners() {
        binding?.btnDone?.setOnClickListener {
            if (isDataValid()) {

            }
        }
    }

    private fun setUpBanks() {
        bankSpinnerAdapter =
                BankSpinnerAdapter(binding!!.spinnerBanks, requireContext())
        bankSpinnerAdapter.let { binding?.spinnerBanks?.setSpinnerAdapter(it) }
        binding?.spinnerBanks?.getSpinnerRecyclerView()?.layoutManager =
                LinearLayoutManager(activity)
        binding?.spinnerBanks?.setOnSpinnerItemSelectedListener<Bank> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerBanks?.dismiss()
        }
        bankSpinnerAdapter.setItems(
                arrayListOf(
                        Bank(1),
                        Bank(1),
                        Bank(1),
                        Bank(1),
                        Bank(1),
                        Bank(1),
                        Bank(1)))
//        viewModel.getServicesCategories()
//                .observe(this, banksResultObserver())
    }

    private fun banksResultObserver(): CustomObserverResponse<List<Bank>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Bank>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<Bank>?
                    ) {
                        data?.let {
                            bankSpinnerAdapter.setItems(it)
                            binding?.spinnerBanks?.selectItemByIndex(0)
                        }
                    }
                })
    }

    private fun isDataValid(): Boolean {
        if (bankSpinnerAdapter.index == -1) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.please_select_the_category)
            )
            return false
        }
        binding?.edIban?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                requireContext()
        )
                .let {
                    if (!it.isValid) {
                        requireActivity().showValidationErrorAlert(
                                title = resources.getString(R.string.product_name),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        binding?.edName?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                requireContext()
        )
                .let {
                    if (!it.isValid) {
                        requireActivity().showValidationErrorAlert(
                                title = resources.getString(R.string.product_description),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        return true
    }


}