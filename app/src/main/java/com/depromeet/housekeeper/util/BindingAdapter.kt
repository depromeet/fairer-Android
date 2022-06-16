package com.depromeet.housekeeper.util

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
  fun loadImage(imageView : ImageView, url:String){
    Glide.with(imageView.context)
      .load(url)
      .placeholder(R.drawable.bg_profile_imageview_inactive)
      .error(R.drawable.bg_profile_imageview_inactive)
      .diskCacheStrategy(DiskCacheStrategy.NONE)
      .apply(RequestOptions().fitCenter())
      .into(imageView)
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
  @androidx.databinding.BindingAdapter("app:imageUrl","app:placeholder")
  @JvmStatic fun loadImage(imageView: ImageView, url: String, placeholder: Drawable){
    Glide.with(imageView.context)
      .load(url)
      .placeholder(placeholder)
      .error(placeholder)
      .diskCacheStrategy(DiskCacheStrategy.NONE)
      .apply(RequestOptions().fitCenter())
      .into(imageView)
  }
}