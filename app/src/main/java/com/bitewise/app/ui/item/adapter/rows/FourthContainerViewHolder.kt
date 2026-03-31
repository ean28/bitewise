package com.bitewise.app.ui.item.adapter.rows

import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFourthBinding
import com.bitewise.app.ui.search.util.ScoreMapper

class FourthContainerViewHolder(
    private val binding: ProductContainerFourthBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        row: ProductRowManager.FourthContainer,
        onClick: ((ProductRowManager.FourthContainer) -> Unit)? = null
    ) {
        val product = row.product
        val context = binding.root.context
        val ecoScore = product.productScores?.ecoScoreGrade ?: "None"

        binding.ivScore.setImageResource(
            ScoreMapper.getEcoDrawable(ecoScore))

        val colorRes = GradeTypes.ECO_SCORE.getColor(ecoScore)
        val colorInt = getColor(context, colorRes)
        val description = GradeTypes.ECO_SCORE.getComment(ecoScore, context.resources)

        binding.txtGrade.text = ecoScore
        binding.txtGrade.setTextColor(getColor(context, colorRes))
        binding.txtDescription.text = description
        binding.txtDescription.setTextColor(colorInt)

        binding.titleRow.setOnClickListener {
            // CONTAINER
            val isVisible = binding.rowContainer.isVisible
            binding.rowContainer.visibility = if (isVisible) View.GONE else View.VISIBLE

            // ARROW
            val targetRotation = if (isVisible) 0f else 90f
            binding.ivArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
        }
    }
}
