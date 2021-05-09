package com.raantech.solalat.provider.ui.barn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.R
import com.raantech.solalat.provider.databinding.RowIndecatorBinding
import com.raantech.solalat.provider.databinding.RowIndecatorImageBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class IndecatorImagesRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Boolean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowIndecatorImageBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowIndecatorImageBinding) :
        BaseViewHolder<Boolean>(binding.root) {

        override fun bind(item: Boolean) {
            if (item) {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.cardview_fill_gray))
            } else {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.white))
            }
        }
    }
}