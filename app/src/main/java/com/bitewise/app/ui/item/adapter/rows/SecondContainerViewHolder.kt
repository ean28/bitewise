package com.bitewise.app.ui.item.adapter.rows

import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerSecondBinding
import com.bitewise.app.domain.Product
import com.bitewise.app.ui.search.util.ScoreMapper

class SecondContainerViewHolder(
    private val binding: ProductContainerSecondBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.SecondContainer,
             onClick: ((ProductRowManager.SecondContainer) -> Unit)? = null
    ) {

        val product = row.product

        binding.ivNutriScore.setImageResource(
            ScoreMapper.getNutriDrawable(product.productScores?.nutritionGrade)
        )
        binding.titleRow.setOnClickListener {
            onClick?.invoke(row)
        }
        binding.txtLearnMore.setOnClickListener {
            onClick?.let {
                println("CLICKED ON LEARN MORE")
            }
        }
    }
}