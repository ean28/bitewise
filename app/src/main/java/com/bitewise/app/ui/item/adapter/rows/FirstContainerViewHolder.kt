package com.bitewise.app.ui.item.adapter.rows

import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.ui.search.util.ScoreMapper

class FirstContainerViewHolder(
    private val binding: ProductContainerFirstBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.FirstContainer,
             onClick: ((ProductRowManager.FirstContainer) -> Unit)? = null
    ) {
        val product = row.product

        binding.ivNutriScore.setImageResource(
            ScoreMapper.getNutriDrawable(product.productScores?.nutritionGrade)
        )
        binding.ivNovaScore.setImageResource(
            ScoreMapper.getNovaDrawable(product.productScores?.novaGroup)
        )
        binding.ivGreenScore.setImageResource(
            ScoreMapper.getEcoDrawable(product.productScores?.ecoScoreGrade)
        )

        binding.txtSummaryContainer.setOnClickListener {
            onClick?.invoke(row)
        }
    }
}