package com.bitewise.app.ui.product.adapter.rows.containers

import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.domain.models.Product
import com.bitewise.app.ui.common.util.loadImage

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
