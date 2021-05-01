package com.raantech.solalat.provider.ui.medical.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.solalat.provider.data.models.medical.response.Medical
import com.raantech.solalat.provider.databinding.RowMedicalServiceBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class MedicalsRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Medical>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowMedicalServiceBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowMedicalServiceBinding) :
        BaseViewHolder<Medical>(binding.root) {

        override fun bind(item: Medical) {
            binding.item = item
            binding.btnCallNow.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }
}