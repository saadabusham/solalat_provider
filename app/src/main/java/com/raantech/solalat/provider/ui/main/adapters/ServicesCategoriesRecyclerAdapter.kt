package com.raantech.solalat.provider.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.data.models.main.home.ServiceCategory
import com.raantech.solalat.provider.databinding.RowServiceCategoryBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class ServicesCategoriesRecyclerAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<ServiceCategory>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowServiceCategoryBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowServiceCategoryBinding) :
            BaseViewHolder<ServiceCategory>(binding.root) {

        override fun bind(item: ServiceCategory) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }
}