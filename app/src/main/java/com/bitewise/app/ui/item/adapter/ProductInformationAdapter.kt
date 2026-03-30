package com.bitewise.app.ui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.databinding.ProductContainerSecondBinding
import com.bitewise.app.ui.item.adapter.rows.*

class ProductInformationAdapter(
    private val rows: List<ProductRowManager>,
    private val onContainerClick: ((ProductRowManager) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_FIRST_CONTAINER = 1
        private const val VIEW_TYPE_SECOND_CONTAINER = 2
    }

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is ProductRowManager.Header -> VIEW_TYPE_HEADER
        is ProductRowManager.FirstContainer -> VIEW_TYPE_FIRST_CONTAINER
        is ProductRowManager.SecondContainer -> VIEW_TYPE_SECOND_CONTAINER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ProductContainerHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_FIRST_CONTAINER -> FirstContainerViewHolder(
                ProductContainerFirstBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_SECOND_CONTAINER -> SecondContainerViewHolder(
                ProductContainerSecondBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val row = rows[position]) {
            is ProductRowManager.Header -> (holder as HeaderViewHolder).bind(row.product)
            is ProductRowManager.FirstContainer -> (holder as FirstContainerViewHolder).bind(row, onContainerClick)
            is ProductRowManager.SecondContainer -> (holder as SecondContainerViewHolder).bind(row, onContainerClick) //removed containerclick to bind params
        }
    }

    override fun getItemCount(): Int = rows.size
}