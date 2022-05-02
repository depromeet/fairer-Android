package com.depromeet.housekeeper.util

import android.graphics.Typeface

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
}