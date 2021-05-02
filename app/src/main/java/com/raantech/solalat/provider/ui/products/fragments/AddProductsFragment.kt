package com.raantech.solalat.provider.ui.products.fragments

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
import com.raantech.solalat.provider.databinding.FragmentAddProductBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.media.MediaActivity
import com.raantech.solalat.provider.ui.products.adapters.CategoriesSpinnerAdapter
import com.raantech.solalat.provider.ui.products.adapters.SmallMediaRecyclerAdapter
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class AddProductsFragment : BaseBindingFragment<FragmentAddProductBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: ProductsViewModel by activityViewModels()

    lateinit var smallMediaRecyclerAdapter: SmallMediaRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_add_product
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
            subTitle = R.string.add_product
        )
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {
        setUpRecyclerView()
        setUpCategories()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgUpload?.setOnClickListener {
            MediaActivity.start(requireActivity(), true, resultLauncher)
        }

        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValid()) {
                viewModel.addProduct(
                    smallMediaRecyclerAdapter.items,
                    categoriesSpinnerAdapter.spinnerItems[categoriesSpinnerAdapter.index],
                    binding?.checkboxReceiveWhatsapp?.isChecked ?: false
                ).observe(this, addProductResultObserver())
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
        binding?.edProductName?.text.toString().validate(
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
        binding?.edProductDescription?.text.toString().validate(
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
        binding?.edProductPrice?.text.toString().validate(
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

        if (smallMediaRecyclerAdapter.itemCount == 0) {
            requireActivity().showErrorAlert(
                resources.getString(R.string.product_images),
                resources.getString(R.string.please_select_the_product_images)
            )
            return false
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

        return true
    }

    private fun setUpRecyclerView() {
        smallMediaRecyclerAdapter = SmallMediaRecyclerAdapter(requireContext())
        binding?.imagesRecyclerView?.adapter = smallMediaRecyclerAdapter
        binding?.imagesRecyclerView?.setOnItemClickListener(this)
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
                    navigationController.navigate(R.id.action_addProductsFragment_to_productsFragment)
                }
            }, showError = true
        )
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                smallMediaRecyclerAdapter.submitItem(data?.getSerializableExtra(Constants.BundleData.MEDIA) as Media)
                binding?.imagesRecyclerView?.smoothScrollToPosition(smallMediaRecyclerAdapter.itemCount - 1)
            }
        }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Media) {
            if (view?.id == R.id.imgRemove) {
                smallMediaRecyclerAdapter.items.removeAt(position)
                smallMediaRecyclerAdapter.notifyItemRemoved(position)
            }
        }
    }


}