package com.raantech.solalat.provider.ui.barn.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.models.map.Address
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.databinding.FragmentAddBarnStep1Binding
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.map.MapActivity
import com.raantech.solalat.provider.ui.media.MediaActivity
import com.raantech.solalat.provider.ui.products.adapters.SmallMediaRecyclerAdapter
import com.raantech.solalat.provider.utils.extensions.showErrorAlert
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.getLocationName
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class EditBarnStep1Fragment : BaseBindingFragment<FragmentAddBarnStep1Binding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BarnViewModel by activityViewModels()

    lateinit var imagesMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    lateinit var logoMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    override fun getLayoutId(): Int = R.layout.fragment_add_barn_step_1
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
        init()
        setUpData()
    }


    private fun setUpData() {
        viewModel.barnToEdit?.let {
            viewModel.productName.postValue(it.name)
            viewModel.address.postValue(Address(it.latitude!!.toDouble(), it.longitude!!.toDouble()))
            viewModel.addressString.postValue(it.address)
            binding?.tvLocation?.text = it.address
            it.additionalImages?.let { it1 -> imagesMediaRecyclerAdapter.submitItems(it1) }
            it.baseImage?.let { it1 -> logoMediaRecyclerAdapter.submitItem(it1) }
        }
    }


    private fun init() {
        setUpLogoRecyclerView()
        setUpImagesRecyclerView()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgUpload?.setOnClickListener {
            MediaActivity.start(requireActivity(), true, imagesResultLauncher)
        }

        binding?.imgLogoUpload?.setOnClickListener {
            if (logoMediaRecyclerAdapter.itemCount == 0)
                MediaActivity.start(requireActivity(), true, logoResultLauncher)
        }

        binding?.btnNext?.setOnClickListener {
            if (isDataValid()) {
                viewModel.logo.clear()
                viewModel.logo.addAll(logoMediaRecyclerAdapter.items)
                viewModel.files.clear()
                viewModel.files.addAll(imagesMediaRecyclerAdapter.items)
                navigationController.navigate(R.id.action_editBarnStep1Fragment_to_editBarnStep2Fragment)
            }
        }
        binding?.tvLocation?.setOnClickListener {
            MapActivity.start(requireActivity(), locationResultLauncher)
        }
    }

    private fun isDataValid(): Boolean {
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
        if (imagesMediaRecyclerAdapter.itemCount == 0) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.barn_images),
                    resources.getString(R.string.please_select_the_product_images)
            )
            return false
        }

        if (logoMediaRecyclerAdapter.itemCount == 0) {
            requireActivity().showErrorAlert(
                    resources.getString(R.string.barn_logo),
                    resources.getString(R.string.please_select_the_barn_logo)
            )
            return false
        }

        return true
    }

    private fun setUpImagesRecyclerView() {
        imagesMediaRecyclerAdapter = SmallMediaRecyclerAdapter(requireContext())
        binding?.imagesRecyclerView?.adapter = imagesMediaRecyclerAdapter
        binding?.imagesRecyclerView?.setOnItemClickListener(object : BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                if (item is Media) {
                    if (view?.id == R.id.imgRemove) {
                        imagesMediaRecyclerAdapter.items.removeAt(position)
                        imagesMediaRecyclerAdapter.notifyItemRemoved(position)
                    }
                }
            }
        })
    }

    private fun setUpLogoRecyclerView() {
        logoMediaRecyclerAdapter = SmallMediaRecyclerAdapter(requireContext())
        binding?.logoRecyclerView?.adapter = logoMediaRecyclerAdapter
        binding?.logoRecyclerView?.setOnItemClickListener(object : BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                if (item is Media) {
                    if (view?.id == R.id.imgRemove) {
                        logoMediaRecyclerAdapter.items.removeAt(position)
                        logoMediaRecyclerAdapter.notifyItemRemoved(position)
                    }
                }
            }
        })
    }

    var imagesResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    imagesMediaRecyclerAdapter.submitItem(data?.getSerializableExtra(Constants.BundleData.MEDIA) as Media)
                    binding?.imagesRecyclerView?.smoothScrollToPosition(imagesMediaRecyclerAdapter.itemCount - 1)
                }
            }

    var logoResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    logoMediaRecyclerAdapter.submitItem(data?.getSerializableExtra(Constants.BundleData.MEDIA) as Media)
                    binding?.logoRecyclerView?.smoothScrollToPosition(logoMediaRecyclerAdapter.itemCount - 1)
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
                imagesMediaRecyclerAdapter.items.removeAt(position)
                imagesMediaRecyclerAdapter.notifyItemRemoved(position)
            }
        }
    }


}