package com.depromeet.housekeeper.util

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView


object BindingAdapter {
  @JvmStatic
  @androidx.databinding.BindingAdapter("app:textStyle")
  fun setTypeface(textView: TextView, style: String) {
    when (style) {
      "bold" -> textView.setTypeface(null, Typeface.BOLD)
      else -> textView.setTypeface(null, Typeface.NORMAL)
    }
  }
  @androidx.databinding.BindingAdapter("app:loadImage")
  @JvmStatic
  fun loadImage(imageView : ImageView, resid:Int ){
    imageView.setImageResource(resid)
  }

  @androidx.databinding.BindingAdapter("app:isSelected")
  @JvmStatic
  fun isSelected(imageView: ImageView, isTrue : Boolean){
    imageView.isSelected = isTrue
  }
}