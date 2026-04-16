package com.bitewise.app.feature.product.ui.adapter.rows.containers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.core.ui.loadImage
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.google.android.material.chip.Chip
import kotlin.math.roundToInt

class HeaderViewHolder(
    private val binding: ProductContainerHeadBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, analysis: AiAnalysisEntity?) {
        binding.imgProductInformation.loadImage(product.imageUrl)
        binding.tvProductName.text = product.name
        binding.txtProductBrand.text = product.brands ?: "Unknown Brand"
        binding.txtProductBarcode.text = product.code
        binding.txtProductQuantity.text = "N/A"

        if (analysis != null) {
            binding.cardAiScore.visibility = View.VISIBLE
            binding.txtAiScoreValue.text = analysis.score.roundToInt().toString()
            binding.txtAiVerifiedLabel.visibility = View.VISIBLE
            
            binding.chipGroupHeadTags.visibility = View.VISIBLE
            binding.chipGroupHeadTags.removeAllViews()
            analysis.dynamicTags.split(",").forEach { tag ->
                val cleanTag = tag.trim()
                if (cleanTag.isNotEmpty()) {
                    val chip = Chip(binding.root.context).apply {
                        text = cleanTag
                        textSize = 10f
                        // Using a standard height if the material resource is missing
                        chipMinHeight = 32f * binding.root.context.resources.displayMetrics.density
                        isClickable = false
                        isCheckable = false
                    }
                    binding.chipGroupHeadTags.addView(chip)
                }
            }
        } else {
            binding.cardAiScore.visibility = View.GONE
            binding.txtAiVerifiedLabel.visibility = View.GONE
            binding.chipGroupHeadTags.visibility = View.GONE
        }
    }
}