package com.bitewise.app.feature.settings.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.databinding.ItemAiAnalysisBinding

class AiAnalysisAdapter : ListAdapter<AiAnalysisEntity, AiAnalysisAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemAiAnalysisBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AiAnalysisEntity) {
            binding.txtBarcode.text = "Barcode: ${item.barcode}"
            binding.txtScore.text = "Score: ${item.score}"
            binding.txtSummary.text = item.summary
            binding.txtTags.text = "Tags: ${item.dynamicTags}"
            binding.txtHash.text = "Hash: ${item.userContextHash}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAiAnalysisBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AiAnalysisEntity>() {
        override fun areItemsTheSame(oldItem: AiAnalysisEntity, newItem: AiAnalysisEntity): Boolean {
            return oldItem.barcode == newItem.barcode
        }

        override fun areContentsTheSame(oldItem: AiAnalysisEntity, newItem: AiAnalysisEntity): Boolean {
            return oldItem == newItem
        }
    }
}
