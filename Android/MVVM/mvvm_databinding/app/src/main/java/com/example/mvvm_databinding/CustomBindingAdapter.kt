package com.example.mvvm_databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bind_image")
fun bindImage(view: ImageView, res: Int?) {
    Glide.with(view.context)
        .load(res)
        .into(view)
}

@BindingAdapter("bind_image", "error_image")
fun bindImage(view: ImageView, res: Int?, error: Int) {
    Glide.with(view.context)
        .load(res)
        .error(error)
        .into(view)
}

