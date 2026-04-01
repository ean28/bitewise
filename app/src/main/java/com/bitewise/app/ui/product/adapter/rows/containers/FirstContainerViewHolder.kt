package com.bitewise.app.ui.product.adapter.rows.containers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.ui.common.grade.GradeManager
import androidx.core.view.isVisible
import com.bitewise.app.ui.product.adapter.rows.ProductRowManager

class FirstContainerViewHolder(
    private val binding: ProductContainerFirstBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.FirstContainer,
             onClick: ((ProductRowManager.FirstContainer) -> Unit)? = null
    ) {
        val product = row.product

        binding.ivNutriScore.setImageResource(
            GradeManager.getNutriDrawable(product.productScores?.nutritionGrade)
        )
        binding.ivNovaScore.setImageResource(
            GradeManager.getNovaDrawable(product.productScores?.novaGroup)
        )
        binding.ivGreenScore.setImageResource(
            GradeManager.getEcoDrawable(product.productScores?.ecoScoreGrade)
        )

        binding.txtSummaryContainer.setOnClickListener {
            //COLLAPSE CONTAINER
            binding.scoresLayout.visibility =
                if(binding.scoresLayout.isVisible)
                    View.GONE else View.VISIBLE

            // ARROW
            val targetRotation =
                if (binding.ivArrow.rotation == 90f) 0f else 90f
            binding.ivArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
        }
    }
}
