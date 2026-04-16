package com.bitewise.app.feature.ai.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.databinding.ItemSyncLogBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SyncLogAdapter : ListAdapter<AiSyncLog, SyncLogAdapter.ViewHolder>(DiffCallback) {

    private val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())

    class ViewHolder(private val binding: ItemSyncLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AiSyncLog, dateFormat: SimpleDateFormat) {
            binding.txtLogDate.text = dateFormat.format(Date(item.timestamp))
            
            val detailText = if (item.skippedCount > 0) {
                "${item.itemCount} synced, ${item.skippedCount} skipped"
            } else {
                "${item.itemCount} items synced"
            }
            binding.txtLogDetails.text = detailText
            
            binding.txtLogTokens.text = "${item.totalTokens} tokens"
            binding.txtLogStatus.text = item.status
            
            val statusColor = when(item.status) {
                "SUCCESS" -> 0xFF4CAF50.toInt()
                "FAILED" -> 0xFFF44336.toInt()
                "PARTIAL" -> 0xFFFF9800.toInt()
                else -> 0xFF757575.toInt()
            }
            binding.txtLogStatus.setTextColor(statusColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSyncLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), dateFormat)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AiSyncLog>() {
        override fun areItemsTheSame(oldItem: AiSyncLog, newItem: AiSyncLog): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: AiSyncLog, newItem: AiSyncLog): Boolean = oldItem == newItem
    }
}
