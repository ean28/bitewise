package com.bitewise.app.ui.product.adapter.rows.containers

import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerThirdBinding
import com.bitewise.app.ui.common.grade.GradeManager
import com.bitewise.app.ui.common.grade.GradeTypes
import com.bitewise.app.ui.product.adapter.rows.ProductRowManager

class ThirdContainerViewHolder(
    private val binding: ProductContainerThirdBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        row: ProductRowManager.ThirdContainer,
        onClick: ((ProductRowManager.ThirdContainer) -> Unit)? = null
    ) {
        val product = row.product
        val context = binding.root.context
        val novaGroup = product.productScores?.novaGroup

        binding.ivScore.setImageResource(
            GradeManager.getNovaDrawable(novaGroup))

        val colorRes = GradeManager.getColor(GradeTypes.NOVA_SCORE, novaGroup)
        val colorInt = getColor(context, colorRes)
        val description = GradeManager.getComment(GradeTypes.NOVA_SCORE, novaGroup, context.resources)

        binding.txtGrade.text = novaGroup.toString()
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
