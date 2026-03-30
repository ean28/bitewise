package com.bitewise.app.ui.item.adapter.rows

import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.domain.Product
import com.bitewise.app.ui.helper.loadImage

class HeaderViewHolder(
    private val binding: ProductContainerHeadBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.imgProductInformation.loadImage(product.imageUrl)
        binding.txtProductBarcode.text = product.code
        binding.txtProductBrand.text = product.name
        binding.txtProductQuantity.text = null
        binding.txtProductAllergens.text = null
    }
}