package com.raantech.solalat.provider.ui.media

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.paginate.Paginate
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.GeneralError
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.data.api.response.ResponseWrapper
import com.raantech.solalat.provider.data.common.Constants
import com.raantech.solalat.provider.data.common.CustomObserverResponse
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.databinding.ActivityMediaBinding
import com.raantech.solalat.provider.ui.base.activity.BaseBindingActivity
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.solalat.provider.ui.media.adapters.MediaRecyclerAdapter
import com.raantech.solalat.provider.ui.media.viewmodel.MediaViewModel
import com.raantech.solalat.provider.utils.ImagePickerUtil.Companion.TAKE_USER_IMAGE_REQUEST_CODE
import com.raantech.solalat.provider.utils.extensions.createImageMultipart
import com.raantech.solalat.provider.utils.extensions.gone
import com.raantech.solalat.provider.utils.extensions.visible
import com.raantech.solalat.provider.utils.pickImages
import com.raantech.solalat.provider.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class MediaActivity : BaseBindingActivity<ActivityMediaBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MediaViewModel by viewModels()

    var selectMedia: Boolean? = null

    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    private lateinit var mediaAdapter: MediaRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_media,
            hasToolbar = true,
            hasBackButton = true,
            toolbarView = toolbar,
            hasTitle = true,
            showBackArrow = true,
            title = R.string.media
        )
        selectMedia = intent.getBooleanExtra(Constants.BundleData.SELECT_MEDIA, false)
        setUpListeners()
        setUpRecyclerView()
        observeLoading()
        loadMedia()
    }

    private fun setUpListeners() {
        binding?.btnAddMedia?.setOnClickListener {
            pickImages(
                requestCode = TAKE_USER_IMAGE_REQUEST_CODE
            )
        }
    }

    private fun uploadMediaObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    mediaAdapter.clear()
                    loadMedia()
                }
            }
        )
    }

    private fun deleteMediaObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    mediaAdapter.clear()
                    loadMedia()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            TAKE_USER_IMAGE_REQUEST_CODE -> {
                val fileUri = data?.data
                fileUri?.path?.let {
                    viewModel.uploadMedia(it.createImageMultipart("file"))
                        .observe(this, uploadMediaObserver())
                }
            }
        }
    }


    private fun loadMedia() {
        viewModel.getMedia(mediaAdapter.itemCount).observe(this, mediaObserver())
    }

    private fun setUpRecyclerView() {
        mediaAdapter = MediaRecyclerAdapter(this)
        binding?.recyclerView?.adapter = mediaAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && mediaAdapter.itemCount > 0 && !isFinished) {
                    loadMedia()
                }
            }

            override fun isLoading(): Boolean {
                return loading.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isFinished
            }

        })
            .setLoadingTriggerThreshold(1)
            .addLoadingListItem(false)
            .build()
    }

    private fun hideShowNoData() {
        if (mediaAdapter.itemCount == 0) {
            binding?.recyclerView?.gone()
            binding?.layoutNoData?.linearNoData?.visible()
        } else {
            binding?.layoutNoData?.linearNoData?.gone()
            binding?.recyclerView?.visible()
        }
    }

    private fun observeLoading() {
        loading.observe(this, Observer {
            if (it) {
                binding?.recyclerView?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.visible()
//                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
//                binding?.layoutShimmer?.shimmerViewContainer?.gone()
//                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.recyclerView?.visible()
            }
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Media
        if (view?.id == R.id.imgRemove) {
            viewModel.deleteMedia(item.id).observe(this, deleteMediaObserver())
        } else {
            if (selectMedia == true) {
                val data = Intent()
                data.putExtra(Constants.BundleData.MEDIA,item)
                setResult(RESULT_OK,data)
            }
        }
    }


    private fun mediaObserver(): CustomObserverResponse<List<Media>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<Media>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<List<Media>>?
                ) {
                    isFinished = data?.body?.isNullOrEmpty() == true
                    data?.body?.let {
                        mediaAdapter.addItems(it)
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    super.onError(subErrorCode, message, errors)
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
                    loading.postValue(true)
                }
            }, true
        )
    }

    companion object {
        private const val requestCode:Int = 101
        fun start(
            context: Activity?
        ) {
            val intent = Intent(context, MediaActivity::class.java)
            context?.startActivity(intent)
        }
        fun start(
            context: Activity?,
            selectMedia: Boolean? = false
        ) {
            val intent = Intent(context, MediaActivity::class.java)
            intent.putExtra(Constants.BundleData.SELECT_MEDIA, selectMedia)
            context?.startActivityForResult(intent, requestCode)
        }
    }

}