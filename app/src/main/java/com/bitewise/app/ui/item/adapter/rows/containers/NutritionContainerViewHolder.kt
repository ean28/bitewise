package com.bitewise.app.ui.item.adapter.rows.containers

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R
import com.bitewise.app.databinding.ProductContainerNutritionBinding
import com.bitewise.app.ui.item.adapter.ProductNutritionTableAdapter
import com.bitewise.app.ui.item.adapter.helpers.DefaultNutritionTable
import com.bitewise.app.ui.item.adapter.helpers.TableLayoutHelper
import com.bitewise.app.ui.item.adapter.rows.ProductRowManager

class NutritionContainerViewHolder(
    private val binding: ProductContainerNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: ProductRowManager.NutritionContainer) {
        val context = binding.root.context

        // Fetch nutrients or fall back to the Default Factory if null/empty
        val rawNutrients = row.product.productNutriments
        val nutrientsList = if (rawNutrients.isNullOrEmpty()) {
            DefaultNutritionTable.createEmptyState()
        } else {
            rawNutrients
        }

        val dataRows = nutrientsList.map { nutrient ->
            val isUnknown = nutrient.unit == "unknown"
            listOf(
                nutrient.name,
                if (isUnknown) "<i>Unavailable</i>" else "${nutrient.amount} ${nutrient.unit}"
            )
        }

        val headers = listOf("Nutrient", "As sold for 100 g")

        val allFirstColumnTexts = dataRows.map { it[0] } + headers[0]
        val maxWidth = TableLayoutHelper.calculateMaxColumnWidth(
            context,
            allFirstColumnTexts,
            R.style.table_row_item
        )

        TableLayoutHelper.populateRow(
            container = binding.tableColumnContainer,
            items = headers,
            firstColumnWidth = maxWidth,
            styleRes = R.style.table_row_item
        )

        binding.tableRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ProductNutritionTableAdapter()
        binding.tableRecyclerView.adapter = adapter

        adapter.updateData(dataRows, maxWidth)

        binding.txtSummaryContainer.setOnClickListener {
            val isCurrentlyVisible = binding.nutriContainer.isVisible
            binding.nutriContainer.isVisible = !isCurrentlyVisible

            val targetRotation = if (isCurrentlyVisible) 0f else 90f
            binding.ivArrow.animate().rotation(targetRotation).setDuration(200).start()
        }
    }
}