package com.bitewise.app.ui.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ItemTableRowBinding
import com.bitewise.app.ui.common.util.TableLayoutHelper

class ProductNutritionTableAdapter(
    private var data: List<List<String>> = emptyList(),
    private var firstColumnWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) : RecyclerView.Adapter<ProductNutritionTableAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTableRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TableLayoutHelper.populateRow(
            container = holder.binding.rowContainer,
            items = data[position],
            firstColumnWidth = firstColumnWidth
        )
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<List<String>>, calculatedWidth: Int) {
        this.data = newData
        this.firstColumnWidth = calculatedWidth
        notifyDataSetChanged()
    }
}
