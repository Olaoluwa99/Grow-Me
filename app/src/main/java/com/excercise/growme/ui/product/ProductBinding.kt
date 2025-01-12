package com.excercise.growme.ui.product

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // Add a placeholder
            .error(android.R.drawable.ic_menu_close_clear_cancel) // Add an error image
            .into(view)
    }
}