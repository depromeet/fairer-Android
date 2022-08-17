package com.depromeet.housekeeper.util

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.model.enums.SignViewType
import java.util.regex.Pattern


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
    fun loadImage(imageView: ImageView, url: String) {
        /*Glide.with(imageView.context)
            .load(url)
            .override(Target.SIZE_ORIGINAL)
            .placeholder(R.drawable.bg_profile_imageview_inactive)
            .error(R.drawable.bg_profile_imageview_inactive)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .fitCenter()
            .into(imageView)*/
        imageView.load(url){
            crossfade(true)
        }
    }

    @androidx.databinding.BindingAdapter("app:signViewType")
    @JvmStatic
    fun signViewType(textView: TextView, signViewType: SignViewType) {
        when (signViewType) {
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

    @androidx.databinding.BindingAdapter("app:imageUrl", "app:placeholder")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String, placeholder: Drawable) {
        /*Glide.with(imageView.context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .override(Target.SIZE_ORIGINAL)
            .placeholder(placeholder)
            .error(placeholder)
            .apply(RequestOptions().fitCenter())
            .into(imageView)*/
        imageView.load(url){
            placeholder(placeholder)
            crossfade(true)
        }

    }

    @androidx.databinding.BindingAdapter("app:setLinkify")
    @JvmStatic
    fun setLinkify(textView: TextView, patternStr: String, linkStr: String) {
        val pattern: Pattern = Pattern.compile(patternStr)
        val transformFilter = Linkify.TransformFilter { _, _ -> "" }
        Linkify.addLinks(textView, pattern, linkStr, null, transformFilter)
    }

    @androidx.databinding.BindingAdapter("app:setSelected")
    @JvmStatic
    fun setSelected(imageButton: ImageButton, selected: Boolean) {
        imageButton.isSelected = selected
    }

}