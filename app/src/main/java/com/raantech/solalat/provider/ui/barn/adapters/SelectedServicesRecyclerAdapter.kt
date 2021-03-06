package com.raantech.solalat.provider.ui.barn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.data.models.barn.respnose.ServicesItem
import com.raantech.solalat.provider.data.models.main.home.Service
import com.raantech.solalat.provider.databinding.RowSelectedServiceBinding
import com.raantech.solalat.provider.databinding.RowServiceBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class SelectedServicesRecyclerAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<ServicesItem>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowSelectedServiceBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowSelectedServiceBinding) :
            BaseViewHolder<ServicesItem>(binding.root) {

        override fun bind(item: ServicesItem) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }
}