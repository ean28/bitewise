package com.bitewise.app.feature.product.ui.adapter.rows.containers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.core.ui.GradeManager
import com.bitewise.app.feature.product.ui.adapter.rows.ProductRowManager

class FirstContainerViewHolder(
    private val binding: ProductContainerFirstBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.FirstContainer,
             onClick: ((ProductRowManager.FirstContainer) -> Unit)? = null
    ) {
        val product = row.product
        val analysis = row.analysis

        if (analysis != null) {
            binding.tvTitle.text = "AI Health Summary"
            binding.txtAiSummaryContent.visibility = View.VISIBLE
            binding.txtAiSummaryContent.text = analysis.summary

        } else {
            binding.tvTitle.text = "Nutri-Impact Preview"
            binding.txtAiSummaryContent.visibility = View.GONE
        }

        binding.ivNutriScore.setImageResource(
            GradeManager.getNutriDrawable(product.productScores?.nutritionGrade)
        )
        binding.ivNovaScore.setImageResource(
            GradeManager.getNovaDrawable(product.productScores?.novaGroup)
        )
        binding.ivGreenScore.setImageResource(
            GradeManager.getEcoDrawable(product.productScores?.ecoScoreGrade)
        )
    }
}
