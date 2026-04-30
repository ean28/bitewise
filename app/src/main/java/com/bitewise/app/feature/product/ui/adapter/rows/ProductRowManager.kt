package com.bitewise.app.feature.product.ui.adapter.rows

import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity

sealed class ProductRowManager(
    open val product: Product,
    open val analysis: AiAnalysisEntity? = null
) {
    data class Header(
        override val product: Product,
        override val analysis: AiAnalysisEntity? = null
    ) : ProductRowManager(product, analysis)

    data class FirstContainer(
        override val product: Product,
        override val analysis: AiAnalysisEntity? = null
    ) : ProductRowManager(product, analysis)

    data class SecondContainer(override val product: Product) : ProductRowManager(product)
    data class ThirdContainer(override val product: Product) : ProductRowManager(product)
    data class FourthContainer(override val product: Product) : ProductRowManager(product)
    data class NutritionContainer(override val product: Product) : ProductRowManager(product)
}
