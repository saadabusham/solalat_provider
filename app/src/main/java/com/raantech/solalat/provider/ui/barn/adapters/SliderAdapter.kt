package com.raantech.solalat.provider.ui.barn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.databinding.RowOnBoardingBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder
import com.raantech.solalat.provider.data.models.auth.onboarding.OnBoardingItem
import com.raantech.solalat.provider.data.models.media.Media
import com.raantech.solalat.provider.databinding.RowSliderBinding

class SliderAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Media>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowSliderBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowSliderBinding) :
        BaseViewHolder<Media>(binding.root) {

        override fun bind(item: Media) {
            binding.data = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it,adapterPosition,item)
            }
        }
    }


}