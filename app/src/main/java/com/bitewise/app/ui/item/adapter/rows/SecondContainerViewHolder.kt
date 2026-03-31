package com.bitewise.app.ui.item.adapter.rows

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerSecondBinding
import com.bitewise.app.ui.search.util.ScoreMapper
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible

class SecondContainerViewHolder(
    private val binding: ProductContainerSecondBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        row: ProductRowManager.SecondContainer,
        onClick: ((ProductRowManager.SecondContainer) -> Unit)? = null
    ) {
        val product = row.product
        val context = binding.root.context
        val nutritionGrade = product.productScores?.nutritionGrade

        binding.ivNutriScore.setImageResource(ScoreMapper.getNutriDrawable(nutritionGrade))

        val colorRes = GradeTypes.NUTRI_SCORE.getColor(nutritionGrade)
        val colorInt = getColor(context, colorRes)
        val description = GradeTypes.NUTRI_SCORE.getComment(nutritionGrade, context.resources)

        binding.tvNutriGrade.text = nutritionGrade?.uppercase()
        binding.tvNutriGrade.setTextColor(getColor(context, colorRes))
        binding.tvNutriDescription.text = description
        binding.tvNutriDescription.setTextColor(colorInt)

        binding.titleRow.setOnClickListener {
            // CONTAINER
            val isVisible = binding.nutritionRow.isVisible
            binding.nutritionRow.visibility = if (isVisible) View.GONE else View.VISIBLE

            // ARROW
            val targetRotation = if (isVisible) 0f else 90f
            binding.ivArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
        }
    }
}
