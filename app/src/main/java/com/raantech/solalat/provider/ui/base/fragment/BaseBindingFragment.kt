package com.raantech.solalat.provider.ui.base.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.solalat.provider.ui.base.activity.IBaseBindingActivity
import com.raantech.solalat.provider.ui.base.dialogs.CustomDialogUtils
import com.raantech.solalat.provider.ui.base.dialogs.progressdialog.ProgressDialogUtil
import com.raantech.solalat.provider.utils.HandleRequestFailedUtil
import com.raantech.solalat.provider.utils.extensions.longToast
import com.raantech.solalat.provider.utils.extensions.refreshLocal
import com.raantech.solalat.provider.utils.extensions.visible
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseBindingFragment<BINDING : ViewDataBinding> : Fragment(),
    IBaseBindingFragment, OnLocaleChangedListener {

    private var toolbar: Toolbar? = null
    protected var binding: BINDING? = null
    protected lateinit var navigationController: NavController

    var rootView: View? = null
    private var shouldCallVisibleView = true
    open fun refreshData() {}
    lateinit var customDialogUtils: CustomDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.refreshLocal()
        customDialogUtils =
            CustomDialogUtils(requireActivity(), withProgress = true, isShowNow = false)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null || shouldRefreshPageWhenReturn()) {
            when {
                binding != null -> {
                    rootView = binding?.root
                }
                isBindingEnabled() -> {
                    binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
                    binding?.lifecycleOwner = this
                    rootView = binding?.root
                }
                else -> {
                    rootView = inflater.inflate(getLayoutId(), container, false)
                }
            }
            shouldCallVisibleView = true
        } else {
            shouldCallVisibleView = false
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasNavigation())
            try {
                navigationController = Navigation.findNavController(view)
            } catch (e: Exception) {

            }
        if (shouldCallVisibleView) {
            onViewVisible()
        }

    }


    override fun startActivity(intent: Intent?) =
        super.startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )


    override fun startActivityForResult(intent: Intent?, requestCode: Int) =
        super.startActivityForResult(
            intent, requestCode, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )

    override fun showLoadingView() {
        customDialogUtils.showProgress()
    }

    override fun hideLoadingView() {
        customDialogUtils.hideProgress()
    }

    override fun showProgressDialog(title: String, message: String, isRemovable: Boolean) {
        activity?.let {
            ProgressDialogUtil.showProgressDialog(it, title, message, isRemovable)
        }
    }

    override fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    ) {
        activity?.let {
            HandleRequestFailedUtil.handleRequestFailedMessages(
                it,
                subErrorCode,
                requestMessage
            )
        }
    }

    override fun addToolbar(
        toolbarView: Toolbar?,
        hasToolbar: Boolean,
        hasBackButton: Boolean,
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasSubTitle: Boolean,
        subTitle: Int,
        showBackArrow: Boolean,
        navigationIcon: Int?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).addToolbar(
                toolbarView = toolbarView,
                hasToolbar = hasToolbar,
                hasBackButton = hasBackButton,
                hasTitle = hasTitle,
                title = title,
                titleString = titleString,
                hasSubTitle = hasSubTitle,
                subTitle = subTitle,
                showBackArrow = showBackArrow,
                navigationIcon = navigationIcon
            )
        }
    }

    open fun addToolbarLocale(
        toolbarView: Toolbar?,
        hasBackButton: Boolean,
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasSubTitle: Boolean,
        subTitle: Int,
        navigationIcon: Int?
    ) {
        toolbar = toolbarView ?: binding?.root?.findViewById(R.id.toolbar)

        if (toolbar == null) return
        if (hasTitle) {
            toolbar?.tvTitle?.text =
                if (titleString.isNullOrEmpty()) getString(title) else titleString
        }

        if (hasSubTitle) {
            toolbar?.tvSubTitle?.text = getString(subTitle)
            toolbar?.tvSubTitle?.visible()
        }

        if (hasBackButton) {
            if (navigationIcon != null) {
                toolbar?.navigationIcon = ContextCompat.getDrawable(
                    requireContext(),
                    navigationIcon
                )
            } else {
                toolbar?.navigationIcon = resources.getDrawable(R.drawable.ic_back)
            }
            toolbar?.setNavigationOnClickListener { v ->
                navigationController.navigateUp()
            }
        } else {
            toolbar?.navigationIcon = null
        }
    }

    override fun updateToolbarTitle(hasTitle: Boolean, title: Int, titleString: String?) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString
            )
        }
    }

    override fun updateToolbarTitle(
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasBackButton: Boolean,
        backArrowTint: Int?,
        showBackArrow: Boolean
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString, hasBackButton, backArrowTint, showBackArrow
            )
        }
    }

    override fun updateToolbarSubTitle(
        hasSubTitle: Boolean,
        subTitle: Int,
        subTitleString: String?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarSubTitle(
                hasSubTitle, subTitle, subTitleString
            )
        }
    }

    fun updateDrawer(enableDrawer: Boolean) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateDrawer(enableDrawer)
        }
    }

    override fun onAfterLocaleChanged() {

    }

    override fun onBeforeLocaleChanged() {

    }

    fun handleError(throwable: Throwable?) {
        when (throwable) {
            is IOException -> {
                longToast(getString(R.string.error_no_internet))
            }
            is SocketTimeoutException -> {
                longToast(getString(R.string.error_server))
            }
            is HttpException -> {

            }
            else -> {
                longToast(getString(R.string.error_msg))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}