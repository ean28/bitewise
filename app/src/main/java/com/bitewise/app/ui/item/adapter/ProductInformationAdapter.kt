package com.bitewise.app.ui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.databinding.ProductContainerFirstBinding
import com.bitewise.app.databinding.ProductContainerHeadBinding
import com.bitewise.app.domain.Product
import com.bitewise.app.ui.helper.loadImage
import com.bitewise.app.ui.search.util.ScoreMapper

class ProductInformationAdapter(
    private val product: Product,
    private val onContainerClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PRODUCT_HEADER = 0
        private const val PRODUCT_FIRST_CONTAINER = 1
    }

    //////// VIEW HOLDERS ////////////
    inner class HeaderViewHolder(
        private val binding: ProductContainerHeadBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.imgProductInformation.loadImage(product.imageUrl)
            binding.txtProductBarcode.text = product.code
            binding.txtProductBrand.text = product.name // or actual brand
            binding.txtProductQuantity.text = "N/A"
            binding.txtProductAllergens.text = "N/A"
            // header is NOT clickable
        }
    }

    inner class FirstContainerViewHolder(
        private val binding: ProductContainerFirstBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.ivNutriScore.setImageResource(
                ScoreMapper.getNutriDrawable(product.productScores?.nutritionGrade)
            )
            binding.ivNovaScore.setImageResource(
                ScoreMapper.getNovaDrawable(product.productScores?.novaGroup)
            )
            binding.ivGreenScore.setImageResource(
                ScoreMapper.getEcoDrawable(product.productScores?.ecoScoreGrade)
            )

            binding.root.setOnClickListener {
                onContainerClick?.invoke(product)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == 0) PRODUCT_HEADER else PRODUCT_FIRST_CONTAINER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PRODUCT_HEADER -> HeaderViewHolder(
                ProductContainerHeadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PRODUCT_FIRST_CONTAINER -> FirstContainerViewHolder(
                ProductContainerFirstBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(product)
            is FirstContainerViewHolder -> holder.bind(product)
        }
    }

    override fun getItemCount(): Int = 2

}