package com.bitewise.app.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ItemTileSearchItemsBinding
import com.bitewise.app.core.ui.loadImage
import com.bitewise.app.feature.search.SearchTileUiState

class SearchTileAdapter(
    private val onItemClick: (SearchTileUiState) -> Unit
) : ListAdapter<SearchTileUiState, SearchTileAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: ItemTileSearchItemsBinding,
        private val onItemClick: (SearchTileUiState) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: SearchTileUiState? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let(onItemClick)
            }
        }

        fun bind(uiState: SearchTileUiState) {
            currentItem = uiState

            binding.txtSearchTileName.text = uiState.name
            binding.imgSearchTile.loadImage(uiState.imageUrl)

            binding.lblSearchNutriscore.setImageResource(uiState.nutriGradeRes)
            binding.lblSearchNovascore.setImageResource(uiState.novaGradeRes)
            binding.lblSearchGreenscore.setImageResource(uiState.ecoGradeRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTileSearchItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SearchTileUiState>() {
        override fun areItemsTheSame(oldItem: SearchTileUiState, newItem: SearchTileUiState): Boolean {
            return oldItem.product.code == newItem.product.code
        }

        override fun areContentsTheSame(oldItem: SearchTileUiState, newItem: SearchTileUiState): Boolean {
            return oldItem == newItem
        }
    }
}
