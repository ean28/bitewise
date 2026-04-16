package com.bitewise.app.core.ui

import android.widget.ImageView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.error
import com.bitewise.app.R

fun ImageView.loadImage(
    url: String?,
    placeholderRes: Int = R.drawable.ic_image_placeholder,
    errorRes: Int = R.drawable.ic_image_error
) {
    this.load(url) {
        crossfade(true)
        placeholder(placeholderRes)
        error(errorRes)
    }
}
