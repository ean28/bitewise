package com.bitewise.app.feature.ai.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R
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
            
            val colorRes = when(item.status) {
                "SUCCESS" -> R.color.sync_success
                "FAILED" -> R.color.sync_failed
                "PARTIAL" -> R.color.sync_partial
                else -> R.color.sync_default
            }
            binding.txtLogStatus.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
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
