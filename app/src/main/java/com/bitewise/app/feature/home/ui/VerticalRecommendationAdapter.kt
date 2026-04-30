package com.bitewise.app.feature.home.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R
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

            val context = binding.root.context
            when (scoreInt) {
                in 80..100 -> {
                    binding.txtScoreValue.setTextColor(ContextCompat.getColor(context, R.color.tv_score_recommended))
                    binding.containerScore.setBackgroundColor(ContextCompat.getColor(context, R.color.score_recommended))
                    binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_recommended))
                }
                in 50 until 80 -> {
                    binding.txtScoreValue.setTextColor(ContextCompat.getColor(context, R.color.tv_score_slightly_recommended))
                    binding.containerScore.setBackgroundColor(ContextCompat.getColor(context, R.color.score_slightly_recommended))
                    binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_slightly_recommended))
                }
                in 10 until 50 -> {
                    binding.txtScoreValue.setTextColor(ContextCompat.getColor(context, R.color.tv_score_not_recommended))
                    binding.containerScore.setBackgroundColor(ContextCompat.getColor(context, R.color.score_not_recommended))
                    binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_not_recommended))
                }
                else -> {
                    binding.txtScoreValue.setTextColor(ContextCompat.getColor(context, R.color.tv_score_dangerous))
                    binding.containerScore.setBackgroundColor(ContextCompat.getColor(context, R.color.score_dangerous))
                    binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_dangerous))
                }
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
