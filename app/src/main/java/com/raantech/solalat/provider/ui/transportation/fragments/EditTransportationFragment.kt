package com.raantech.solalat.provider.ui.transportation.fragments

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
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.data.models.product.response.ServiceCategoriesResponse
import com.raantech.solalat.provider.data.models.product.response.ServiceCategory
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.databinding.FragmentAddTransportationBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.map.MapActivity
import com.raantech.solalat.provider.ui.media.MediaActivity
import com.raantech.solalat.provider.ui.products.adapters.CategoriesSpinnerAdapter
import com.raantech.solalat.provider.ui.products.adapters.SmallMediaRecyclerAdapter
import com.raantech.solalat.provider.ui.transportation.adapters.GeneralStringDropDownAdapter
import com.raantech.solalat.provider.ui.transportation.dialogs.CitiesBottomSheet
import com.raantech.solalat.provider.ui.transportation.viewmodels.TransportationViewModel
import com.raantech.solalat.provider.utils.DateTimeUtil.YEAR_NUMBER_FORMATTING
import com.raantech.solalat.provider.utils.extensions.*
import com.raantech.solalat.provider.utils.getLocationName
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class EditTransportationFragment : BaseBindingFragment<FragmentAddTransportationBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: TransportationViewModel by activityViewModels()

    lateinit var smallMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    lateinit var yearsDropDownAdapter: GeneralStringDropDownAdapter
    override fun getLayoutId(): Int = R.layout.fragment_add_transportation
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
                subTitle = R.string.edit_transportation_services
        )
        setUpBinding()
        setUpListeners()
        init()
        setUpData()
    }

    private fun setUpData() {
        viewModel.transpornToEdit?.let {
            viewModel.productName.postValue(it.name)
            viewModel.address.postValue(Address(it.latitude!!.toDouble(), it.longitude!!.toDouble()))
            viewModel.addressString.postValue(it.address)
            binding?.tvLocation?.text = it.address
            viewModel.plateNumber.postValue(it.truckNumber)
            it.additionalImages?.let { it1 -> smallMediaRecyclerAdapter.submitItems(it1) }
            it.cities?.let { it1 ->
                viewModel.cities.clear()
                viewModel.cities.addAll(it1)
            }
            binding?.tvSelectCities?.text = viewModel.cities.map { it.name }.joinToString()
            binding?.checkboxGlobalTransport?.isChecked = it.availableIt ?: false
            viewModel.phoneNumber.postValue(it.contactNumber?.checkPhoneNumberFormat())
            binding?.checkboxReceiveWhatsapp?.isChecked = it.receivedWhatsapp ?: false
        }
    }

    private fun init() {
        setUpRecyclerView()
        setUpCategories()
        setUpYearsAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnAddTransport?.text = resources.getString(R.string.save_changes)
        viewModel.selectedCountryCode.postValue(binding?.countryCodePicker?.defaultCountryCode)
        binding?.countryCodePicker?.setOnCountryChangeListener {
            viewModel.selectedCountryCode.postValue(it.phoneCode)
        }
        binding?.imgUpload?.setOnClickListener {
            MediaActivity.start(requireActivity(), true, mediaResultLauncher)
        }

        binding?.btnAddTransport?.setOnClickListener {
            if (isDataValid()) {
                viewModel.updateTransportation(viewModel.getTransportationRequest(
                        smallMediaRecyclerAdapter.items.map { it.id },
                        categoriesSpinnerAdapter.spinnerItems[categoriesSpinnerAdapter.index].id,
                        yearsDropDownAdapter.spinnerItems[yearsDropDownAdapter.index].toInt(),
                        binding?.checkboxReceiveWhatsapp?.isChecked ?: false,
                        binding?.checkboxGlobalTransport?.isChecked ?: false)
                ).observe(this, updateTransportationResultObserver())
            }
        }
        binding?.tvSelectCities?.setOnClickListener {
            CitiesBottomSheet(object : CitiesBottomSheet.CityPickerCallBack {
                override fun callBack(selectedCities: List<City>) {
                    viewModel.cities.clear()
                    viewModel.cities.addAll(selectedCities)
                    binding?.tvSelectCities?.text = selectedCities.map { it.name }.joinToString()
                }
            }, viewModel, viewModel.cities).show(childFragmentManager, "CitiesPicker")
        }
        binding?.tvLocation?.setOnClickListener {
            MapActivity.start(requireActivity(), locationResultLauncher)
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
        if (yearsDropDownAdapter.index == -1) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.please_select_the_manufacturing_year)
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
                                title = resources.getString(R.string.product_name),
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
        binding?.edPlateNumber?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                requireContext()
        )
                .let {
                    if (!it.isValid) {
                        requireActivity().showValidationErrorAlert(
                                title = resources.getString(R.string.plate_number),
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

        if (viewModel.cities.size == 0) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.cities),
                    resources.getString(R.string.please_select_the_provided_cities)
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
        viewModel.getServicesCategories()
                .observe(this, categoriesResultObserver())
    }


    private fun setUpYearsAdapter() {
        yearsDropDownAdapter = GeneralStringDropDownAdapter(binding!!.spinnerYear, requireContext())
        yearsDropDownAdapter.let { binding?.spinnerYear?.setSpinnerAdapter(it) }
        binding?.spinnerYear?.getSpinnerRecyclerView()?.layoutManager =
                LinearLayoutManager(activity)
        val yearsList = mutableListOf<String>()
        for (i in -1..30) {
            if (i == -1) {
                getCurrentDate().toDate()?.incrementDateByYear(1)?.time?.millisecondFormatting(
                        YEAR_NUMBER_FORMATTING
                )
                        ?.let { yearsList.add(it) }
            } else
                getCurrentDate().toDate()?.decrementDateByYear(i)?.time?.millisecondFormatting(
                        YEAR_NUMBER_FORMATTING
                )
                        ?.let { yearsList.add(it) }
        }
        yearsDropDownAdapter.setItems(yearsList)
        binding?.spinnerYear?.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerYear?.dismiss()
        }
        yearsDropDownAdapter.spinnerItems.withIndex().singleOrNull { it.value == viewModel.transpornToEdit?.manufacturingYear }?.index?.let { binding?.spinnerYear?.selectItemByIndex(it) }
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
                            categoriesSpinnerAdapter.spinnerItems.withIndex().singleOrNull { it1 -> it1.value.id == viewModel.transpornToEdit?.category?.id }?.let {
                                it.value.selected = true
                                binding?.spinnerCategory?.selectItemByIndex(it.index)
                            }
                        }
                    }
                })
    }

    private fun updateTransportationResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<Any> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: Any?
                    ) {
                        navigationController.navigateUp()
                    }
                }, showError = true
        )
    }

    var mediaResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    smallMediaRecyclerAdapter.submitItem(data?.getSerializableExtra(Constants.BundleData.MEDIA) as Media)
                    binding?.imagesRecyclerView?.smoothScrollToPosition(smallMediaRecyclerAdapter.itemCount - 1)
                }
            }
    var locationResultLauncher =
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

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Media) {
            if (view?.id == R.id.imgRemove) {
                smallMediaRecyclerAdapter.items.removeAt(position)
                smallMediaRecyclerAdapter.notifyItemRemoved(position)
            }
        }
    }


}