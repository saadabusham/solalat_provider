package com.raantech.solalat.provider.ui.barn.fragments

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.databinding.FragmentBarnDetailsBinding
import com.raantech.solalat.provider.ui.barn.adapters.IndecatorImagesRecyclerAdapter
import com.raantech.solalat.provider.ui.barn.adapters.SelectedServicesRecyclerAdapter
import com.raantech.solalat.provider.ui.barn.adapters.SliderAdapter
import com.raantech.solalat.provider.ui.barn.viewmodels.BarnViewModel
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.base.fragment.BaseBindingFragment
import com.raantech.solalat.provider.ui.dataview.viewimage.ViewImageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.row_image_view.view.*

@AndroidEntryPoint
class BarnDetailsFragment : BaseBindingFragment<FragmentBarnDetailsBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BarnViewModel by activityViewModels()

    lateinit var indicatorRecyclerAdapter: IndecatorImagesRecyclerAdapter
    lateinit var onBoardingAdapter: SliderAdapter
    private var indicatorPosition = 0

    lateinit var servicesRecyclerAdapter: SelectedServicesRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_barn_details

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
                subTitle = R.string.preview_data
        )
        setUpBinding()
        setUpListeners()
        init()
    }

    private fun init() {
        setUpRecyclerView()
        setUpPager()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpPager() {
        onBoardingAdapter = SliderAdapter(requireContext())
        binding?.vpOnBoarding?.adapter =
                onBoardingAdapter.apply {
                    viewModel.barnToEdit?.additionalImages?.let {
                        viewModel.barnToEdit?.baseImage?.let { it1 -> submitItem(it1) }
                        submitItems(it)
                    }
                }
        binding?.vpOnBoarding?.setOnItemClickListener(this)
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndecatorImagesRecyclerAdapter(requireContext())
        binding?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        onBoardingAdapter.items.let {
            it.withIndex().forEach {
                indicatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.vpOnBoarding?.registerOnPageChangeCallback(
                pagerCallback
        )
    }

    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateIndicator(position)
        }
    }

    private fun updateIndicator(position: Int) {
        indicatorRecyclerAdapter.items[indicatorPosition] = false
        indicatorRecyclerAdapter.items[position] = true
        indicatorRecyclerAdapter.notifyDataSetChanged()
        indicatorPosition = position
    }


    private fun setUpListeners() {
        binding?.btnEditBarn?.setOnClickListener {
            navigationController.navigate(R.id.action_barnDetailsFragment_to_editBarnStep1Fragment,
                    bundleOf(Pair(Constants.BundleData.SERVICE, viewModel.barnToEdit)))
        }
    }

    private fun setUpRecyclerView() {
        servicesRecyclerAdapter = SelectedServicesRecyclerAdapter(requireContext())
        binding?.rvServices?.adapter = servicesRecyclerAdapter
        viewModel.barnToEdit?.services?.let { servicesRecyclerAdapter.submitItems(it) }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Media
        view?.imgMedia?.let { ViewImageActivity.start(requireActivity(), item.url, it) }
    }

}