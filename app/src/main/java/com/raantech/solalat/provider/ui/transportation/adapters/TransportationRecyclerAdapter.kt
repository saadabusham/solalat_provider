package com.raantech.solalat.provider.ui.transportation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paginate.Paginate
import com.raantech.solalat.provider.data.models.transportation.response.Transportation
import com.raantech.solalat.provider.databinding.RowTransportationBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder
import com.raantech.solalat.provider.utils.extensions.setPopUpAnimation

class TransportationRecyclerAdapter constructor(
        context: Context, val paginateCallBack: Paginate.Callbacks
) : BaseBindingRecyclerViewAdapter<Transportation>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowTransportationBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setPopUpAnimation(position)
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowTransportationBinding) :
            BaseViewHolder<Transportation>(binding.root) {

        override fun bind(item: Transportation) {
            binding.item = item
            if (adapterPosition == itemCount - 1) {
                paginateCallBack.onLoadMore()
            }
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }
}