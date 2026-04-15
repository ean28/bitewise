package com.bitewise.app.feature.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.core.ui.loadImage
import com.bitewise.app.databinding.HomeItemTileHorizontalBinding
import com.bitewise.app.feature.product.api.Product

class HorizontalTileAdapter(
    private val onClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<HorizontalTileAdapter.TileViewHolder>() {

    private val items = mutableListOf<Product>()

    inner class TileViewHolder(
        private val binding: HomeItemTileHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.txtTileName.text = product.name
            binding.imgTile.loadImage(product.imageUrl)

            binding.root.setOnClickListener {
                onClick?.invoke(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val binding = HomeItemTileHorizontalBinding.inflate(
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
