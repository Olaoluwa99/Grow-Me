package com.excercise.growme.model

import android.graphics.drawable.Drawable
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

@BindingAdapter("imageDrawable")
fun loadImage(view: ImageView, image: Drawable?) {
    Glide.with(view.context)
        .load(image)
        .placeholder(android.R.drawable.ic_menu_gallery) // Add a placeholder
        .error(android.R.drawable.ic_menu_close_clear_cancel) // Add an error image
        .into(view)
}