package com.raantech.solalat.provider.ui.more.faqs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.data.models.more.FaqsResponse
import com.raantech.solalat.provider.databinding.RowFaqBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder
import com.raantech.solalat.provider.utils.extensions.*

class FaqsRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<FaqsResponse>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowFaqBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setSlideAnimation(position)
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowFaqBinding) :
        BaseViewHolder<FaqsResponse>(binding.root) {

        override fun bind(item: FaqsResponse) {
            binding.root.setOnClickListener {
                if (!item.isExpanded) {
                    binding.tvDesc.maxLines = 1000
                    binding.ivArrow.halfRotate(true)
                    binding.tvDesc.visible()
                } else {
                    binding.tvDesc.maxLines = 1
                    binding.ivArrow.halfRotate(false)
                    binding.tvDesc.gone()
                }
                item.isExpanded = !item.isExpanded
            }
            binding.tvTitle.text = item.question
            binding.tvDesc.text = item.answer
        }
    }
}