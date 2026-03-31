package com.bitewise.app.ui.item.adapter.helpers

import android.content.Context
import android.text.Html
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.bitewise.app.R

object TableLayoutHelper {

    fun calculateMaxColumnWidth(context: Context, items: List<String>, styleRes: Int): Int {
        val textView = TextView(ContextThemeWrapper(context, styleRes))
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        var maxWidth = 0

        items.forEach { text ->
            textView.text = text
            textView.measure(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (textView.measuredWidth > maxWidth) {
                maxWidth = textView.measuredWidth
            }
        }
        return maxWidth
    }

    fun populateRow(
        container: LinearLayout,
        items: List<String>,
        firstColumnWidth: Int,
        styleRes: Int = R.style.table_row_item,
        isCentered: Boolean = false
    ) {
        container.removeAllViews()
        val context = container.context

        items.forEachIndexed { index, text ->
            val themedContext = ContextThemeWrapper(context, styleRes)
            val textView = TextView(themedContext)

            val width = if (index == 0) firstColumnWidth else 0
            val weight = if (index == 0) 0f else 1f

            val params = LinearLayout.LayoutParams(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                weight
            )

            if (isCentered) {
                textView.gravity = Gravity.CENTER
            }

            if (index > 0) {
                params.marginStart = context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
            }

            textView.layoutParams = params
            textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

            container.addView(textView)
        }
    }
}
