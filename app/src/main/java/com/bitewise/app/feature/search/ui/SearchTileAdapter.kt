package com.bitewise.app.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.databinding.ItemTileSearchItemsBinding
import com.bitewise.app.core.ui.GradeManager
import com.bitewise.app.core.ui.loadImage

class SearchTileAdapter(
    private val onClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<SearchTileAdapter.TileViewHolder>() {

    private val items = mutableListOf<Product>()

    inner class TileViewHolder(
        private val binding: ItemTileSearchItemsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.txtSearchTileName.text = product.name
            binding.imgSearchTile.loadImage(product.imageUrl)

            binding.lblSearchNutriscore.setImageResource(
                GradeManager.getNutriDrawable(product.productScores?.nutritionGrade)
            )

            binding.lblSearchNovascore.setImageResource(
                GradeManager.getNovaDrawable(product.productScores?.novaGroup)
            )

            binding.lblSearchGreenscore.setImageResource(
                GradeManager.getEcoDrawable(null)
            )

            binding.root.setOnClickListener {
                onClick?.invoke(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val binding = ItemTileSearchItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Product>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
