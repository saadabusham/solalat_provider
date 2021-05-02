package com.raantech.solalat.provider.ui.transportation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.data.models.transportation.response.City
import com.raantech.solalat.provider.databinding.RowCityBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class CityRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<City>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowCityBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowCityBinding) :
        BaseViewHolder<City>(binding.root) {

        override fun bind(item: City) {
            binding.city = item
            binding.root.setOnClickListener {
                item.selected = !item.selected!!
                notifyItemChanged(adapterPosition)
            }
        }
    }
}