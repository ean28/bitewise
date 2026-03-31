package com.bitewise.app.ui.item.adapter.rows

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R
import com.bitewise.app.databinding.ProductContainerNutritionBinding
import com.bitewise.app.ui.item.adapter.ProductNutritionTableAdapter
import com.bitewise.app.ui.item.adapter.helpers.TableLayoutHelper

class NutritionContainerViewHolder(
    private val binding: ProductContainerNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.NutritionContainer) {
        val context = binding.root.context

        val headers = listOf("Nutrient", "As sold for 100 g")
        val mockRows = listOf(
            listOf("Energy", "450kcal"),
            listOf("Fat", "20.5g"),
            listOf("Saturated Fat", "5.2g"),
            listOf("Carbohydrates", "60.0g"),
            listOf("Sugars", "12.0g"),
            listOf("Proteins", "8.5g"),
            listOf("Salt", "1.2g"),
            listOf("Proteins", "8.5g"),
            listOf("Salt", "1.2g"),
            listOf("Proteins", "8.5g"),
            listOf("Salt", "1.2g"),
            listOf("Proteins", "8.5g"),
            listOf("Salt", "1.2g"),
            listOf("Salt", "1.2g"),
            listOf("Salt", "1.2g"),
            listOf("Salt", "1.2g"),
            listOf("Salt", "1.2g"),
            listOf("Salt", "1.2g")
        )

        val allFirstColumnTexts = mockRows.map { it[0] } + headers[0]
        val maxWidth = TableLayoutHelper.calculateMaxColumnWidth(
            context,
            allFirstColumnTexts,
            R.style.table_row_item
        )

        TableLayoutHelper.populateRow(
            container = binding.tableRowContainer,
            items = headers,
            firstColumnWidth = maxWidth,
            styleRes = R.style.table_row_item
        )

        binding.tableRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ProductNutritionTableAdapter()
        binding.tableRecyclerView.adapter = adapter

        adapter.updateData(mockRows, maxWidth)

        binding.txtSummaryContainer.setOnClickListener {
            val isCurrentlyVisible = binding.nutriContainer.isVisible
            binding.nutriContainer.isVisible = !isCurrentlyVisible

            val targetRotation = if (isCurrentlyVisible) 0f else 90f
            binding.ivArrow.animate().rotation(targetRotation).setDuration(200).start()
        }
    }
}