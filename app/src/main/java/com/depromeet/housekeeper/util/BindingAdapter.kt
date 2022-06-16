package com.depromeet.housekeeper.util

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.model.enums.SignViewType


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

  @androidx.databinding.BindingAdapter("app:signViewType")
  @JvmStatic
  fun signViewType(textView: TextView, signViewType:SignViewType){
    when(signViewType){
      SignViewType.UserName -> {
        textView.setText(R.string.sign_name_text)
      }
      SignViewType.GroupName -> {
        textView.setText(R.string.group_name_text)
      }
      SignViewType.InviteCode -> {
        textView.setText(R.string.invite_code_text)
      }
      SignViewType.ModifyGroupName -> {
        textView.setText(R.string.modify_group_name_text)
      }
    }
  }
}