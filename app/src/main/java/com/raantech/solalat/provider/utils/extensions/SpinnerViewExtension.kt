package com.raantech.solalat.provider.utils.extensions

import androidx.databinding.BindingAdapter
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.common.CommonEnums
import com.raantech.solalat.provider.utils.LocaleUtil
import com.skydoves.powerspinner.PowerSpinnerView
import com.skydoves.powerspinner.SpinnerGravity

@BindingAdapter("arrow_gravity")
fun PowerSpinnerView.setArrowGravity(boolean: Boolean) {
   this.arrowGravity = if(LocaleUtil.getLanguage() == CommonEnums.Languages.Arabic.value) SpinnerGravity.START else SpinnerGravity.END
   this.arrowTint = resources.getColor(R.color.circle_image_stroke_color)
}

