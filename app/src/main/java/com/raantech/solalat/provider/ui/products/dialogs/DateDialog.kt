package com.raantech.solalat.provider.ui.products.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.databinding.DialogDateBinding
import com.raantech.solalat.provider.ui.base.dialogs.DialogsUtil
import com.raantech.solalat.provider.ui.products.viewmodels.ProductsViewModel
import com.raantech.solalat.provider.utils.DateTimeUtil
import com.raantech.solalat.provider.utils.extensions.millisecondFormatting
import com.raantech.solalat.provider.utils.extensions.showValidationErrorAlert
import com.raantech.solalat.provider.utils.extensions.validate
import com.raantech.solalat.provider.utils.validation.ValidatorInputTypesEnums
import java.util.*

class DateDialog(
        private val activity: Activity,
        private val lifecycle: LifecycleOwner,
        private val callBack: CallBack
) :
        Dialog(activity) {

    private lateinit var binding: DialogDateBinding
    val date: MutableLiveData<String> = MutableLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        binding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(activity),
                        R.layout.dialog_date,
                        null,
                        false
                )
        binding.viewModel = this
        binding.lifecycleOwner = lifecycle
        setContentView(binding.root)
        setCancelable(true)
    }

    fun onCancelClicked() {
        cancel()
    }

    fun onSubmitClicked() {
        date.value.toString().validate(
                ValidatorInputTypesEnums.DATE,
                activity
        )
                .let {
                    if (!it.isValid) {
                        activity.showValidationErrorAlert(title = it.errorTitle, message = it.errorMessage)
                    } else {
                        callBack.callBack(date = date.value.toString())
                        cancel()
                    }
                }
    }
    fun onDateClicked() {
        activity.let {
            DialogsUtil.showDatePickerDialog(
                    context = it,
                    calBefore = Calendar.getInstance(),
                    minDate = Calendar.getInstance().time.time,
                    showDays = true, showMonths = true, showYears = true,
                    maxDate = null,
                    listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        handleUserSelectIssueDate(
                                dayOfMonth,
                                month,
                                year
                        )
                    })
        }
    }

    fun handleUserSelectIssueDate(dayOfMonth: Int, month: Int, year: Int) {
        DateTimeUtil.getDateInMillis(
                year,
                month,
                dayOfMonth
        ).millisecondFormatting(DateTimeUtil.YEAR_MONTH_DAY_DATE_TIME_FORMATTING).also {
            date.postValue(it)
        }
    }

    interface CallBack {
        fun callBack(date: String)
    }

}
