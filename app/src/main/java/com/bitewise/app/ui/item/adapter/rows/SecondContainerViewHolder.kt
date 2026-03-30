package com.bitewise.app.ui.item.adapter.rows

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerSecondBinding
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
        binding.tvNutriGrade.text = product.productScores?.nutritionGrade

        binding.titleRow.setOnClickListener {
            // COLLAPSE CONTAINER
            binding.nutritionRow.visibility =
                if(binding.nutritionRow.visibility == View.VISIBLE)
                    View.GONE else View.VISIBLE

            // ARROW
            val targetRotation =
                if (binding.ivArrow.rotation == 90f)
                    0f else 90f
            binding.ivArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
        }

    }
}