package com.bitewise.app.ui.helper

import android.widget.ImageView
import coil3.load
import coil3.request.placeholder
import coil3.request.error
import com.bitewise.app.R

fun ImageView.loadImage(url: String?) {
    this.load(url) {
        placeholder(R.drawable.ic_image_placeholder)
        error(R.drawable.ic_image_error)
    }
}