package com.raantech.solalat.provider.ui.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paginate.Paginate
import com.raantech.solalat.provider.data.models.product.response.product.Product
import com.raantech.solalat.provider.databinding.RowProductGridBinding
import com.raantech.solalat.provider.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.solalat.provider.ui.base.adapters.BaseViewHolder

class ProductsGridRecyclerAdapter(
        context: Context, val paginateCallBack: Paginate.Callbacks
) : BaseBindingRecyclerViewAdapter<Product>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowProductGridBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowProductGridBinding) :
            BaseViewHolder<Product>(binding.root) {

        override fun bind(item: Product) {
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