package com.depromeet.housekeeper.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import com.depromeet.housekeeper.R

class FairerDialog(context: Context) {
    lateinit var onItemClickListener: OnItemClickListener

    private val dialog = Dialog(context)

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun showDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_fairer)

        val btnDialogCancel = dialog.findViewById<AppCompatButton>(R.id.dialog_fairer_cancel_btn)
        val btnDialogOk = dialog.findViewById<AppCompatButton>(R.id.dialog_fairer_ok_btn)

        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDialogOk.setOnClickListener {
            onItemClickListener.onItemClick()
            dialog.dismiss()
        }

        dialog.show()
    }

    interface OnDialogClickListener {
        fun onClicked(name: String)
    }
}