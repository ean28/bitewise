package com.bitewise.app.feature.product.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductNutritionTableItemBinding
import com.bitewise.app.feature.product.api.Nutrient

class ProductNutritionTableAdapter(
    private val items: List<Nutrient>
) : RecyclerView.Adapter<ProductNutritionTableAdapter.RowViewHolder>() {

    class RowViewHolder(private val binding: ProductNutritionTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(n: Nutrient) {
            binding.txtNutrientName.text = n.name
            binding.txtNutrientAmount.text = "${n.amount} ${n.unit}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val binding = ProductNutritionTableItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
