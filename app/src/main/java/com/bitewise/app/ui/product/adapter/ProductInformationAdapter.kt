package com.bitewise.app.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.databinding.ProductContainerNutritionBinding
import com.bitewise.app.databinding.ProductContainerSecondBinding
import com.bitewise.app.databinding.ProductContainerThirdBinding
import com.bitewise.app.databinding.ProductContainerFourthBinding
import com.bitewise.app.ui.product.adapter.rows.ProductRowManager
import com.bitewise.app.ui.product.adapter.rows.containers.FirstContainerViewHolder
import com.bitewise.app.ui.product.adapter.rows.containers.FourthContainerViewHolder
import com.bitewise.app.ui.product.adapter.rows.containers.HeaderViewHolder
import com.bitewise.app.ui.product.adapter.rows.containers.NutritionContainerViewHolder
import com.bitewise.app.ui.product.adapter.rows.containers.SecondContainerViewHolder
import com.bitewise.app.ui.product.adapter.rows.containers.ThirdContainerViewHolder

class ProductInformationAdapter(
    private val rows: List<ProductRowManager>,
    private val onContainerClick: ((ProductRowManager) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_FIRST_CONTAINER = 1
        private const val VIEW_TYPE_SECOND_CONTAINER = 2
        private const val VIEW_TYPE_THIRD_CONTAINER = 3
        private const val VIEW_TYPE_FOURTH_CONTAINER = 4
        private const val VIEW_TYPE_NUTRITION_CONTAINER = 5
    }

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is ProductRowManager.Header -> VIEW_TYPE_HEADER
        is ProductRowManager.FirstContainer -> VIEW_TYPE_FIRST_CONTAINER
        is ProductRowManager.SecondContainer -> VIEW_TYPE_SECOND_CONTAINER
        is ProductRowManager.ThirdContainer -> VIEW_TYPE_THIRD_CONTAINER
        is ProductRowManager.FourthContainer -> VIEW_TYPE_FOURTH_CONTAINER
        is ProductRowManager.NutritionContainer -> VIEW_TYPE_NUTRITION_CONTAINER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ProductContainerHeadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_FIRST_CONTAINER -> FirstContainerViewHolder(
                ProductContainerFirstBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_SECOND_CONTAINER -> SecondContainerViewHolder(
                ProductContainerSecondBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_THIRD_CONTAINER -> ThirdContainerViewHolder(
                ProductContainerThirdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_FOURTH_CONTAINER -> FourthContainerViewHolder(
                ProductContainerFourthBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_NUTRITION_CONTAINER -> NutritionContainerViewHolder(
                ProductContainerNutritionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val row = rows[position]) {
            is ProductRowManager.Header -> (holder as HeaderViewHolder).bind(row.product)
            is ProductRowManager.FirstContainer -> (holder as FirstContainerViewHolder).bind(row, onContainerClick)
            is ProductRowManager.SecondContainer -> (holder as SecondContainerViewHolder).bind(row, onContainerClick)
            is ProductRowManager.ThirdContainer -> (holder as ThirdContainerViewHolder).bind(row, onContainerClick)
            is ProductRowManager.FourthContainer -> (holder as FourthContainerViewHolder).bind(row, onContainerClick)
            is ProductRowManager.NutritionContainer -> (holder as NutritionContainerViewHolder).bind(row)
        }
    }

    override fun getItemCount(): Int = rows.size
}
