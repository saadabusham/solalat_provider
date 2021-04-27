package com.raantech.solalat.provider.ui.base.views

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimation(
        private val view: View,
        private val fromHeight: Int,
        private val fromWidth: Int,
        private val scale: Float,
        duration: Long
) : Animation() {

    init {
        this.duration = duration
    }

    override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
    ) {
        val height = (fromHeight * scale - fromHeight) * interpolatedTime + fromHeight
        val width = (fromWidth * scale - fromWidth) * interpolatedTime + fromWidth
        val layoutParams = view.layoutParams
        layoutParams.height = height.toInt()
        layoutParams.width = width.toInt()
        view.pivotX = 0.toFloat()
        view.pivotY = 0.toFloat()
        view.requestLayout()
    }
}