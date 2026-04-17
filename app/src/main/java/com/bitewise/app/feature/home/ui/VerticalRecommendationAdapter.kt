package com.bitewise.app.feature.home.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.HomeItemTileVerticalBinding
import com.bitewise.app.core.ui.GradeManager
import com.bitewise.app.core.ui.loadImage
import com.bitewise.app.feature.ai.api.ScoredProduct
import kotlin.math.roundToInt

class VerticalRecommendationAdapter(
    private val onClick: ((ScoredProduct) -> Unit)? = null
) : RecyclerView.Adapter<VerticalRecommendationAdapter.ViewHolder>() {

    private val items = mutableListOf<ScoredProduct>()

    inner class ViewHolder(private val binding: HomeItemTileVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: ScoredProduct) {
            val product = state.product
            binding.txtTileName.text = product.name
            binding.txtTileBrand.text = product.brands ?: "Unknown Brand"
            binding.imgTile.loadImage(product.imageUrl)
            
            val gradeDrawable = GradeManager.getNutriDrawable(product.productScores?.nutritionGrade)
            binding.imgNutriGrade.setImageResource(gradeDrawable)

            // Bind Score
            val scoreInt = state.score.roundToInt()
            binding.txtScoreValue.text = scoreInt.toString()
            
            // AI Verified Label visibility
            binding.txtAiLabel.visibility = if (state.analysis != null) View.VISIBLE else View.GONE

            val scoreColor = when {
                scoreInt >= 80 -> "#2E7D32" // Green
                scoreInt >= 50 -> "#F9A825" // Amber
                else -> "#C62828"           // Red
            }
            binding.txtScoreValue.setTextColor(scoreColor.toColorInt())

            // Reset UI state
            binding.root.setCardBackgroundColor(Color.WHITE)
            binding.txtTileName.setTextColor("#212121".toColorInt())
            binding.containerScore.setBackgroundColor("#F5F5F5".toColorInt())

            if (state.isSafetyRisk) {
                binding.root.setCardBackgroundColor("#FFEBEE".toColorInt())
                binding.containerScore.setBackgroundColor("#FFCDD2".toColorInt())
                binding.txtTileName.text = "⚠️ HIGH RISK: ${product.name}"
                binding.txtTileBrand.text = state.safetyReasoning ?: "Allergen Detected"
            }

            binding.root.setOnClickListener {
                onClick?.invoke(state)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeItemTileVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<ScoredProduct>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
