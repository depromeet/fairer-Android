package com.depromeet.housekeeper.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.depromeet.housekeeper.R

class FairerDialog(private val context: Context, private val type: DialogType) {
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
        val tvDialogTitle = dialog.findViewById<TextView>(R.id.dialog_fairer_title_tv)
        val tvDialogDesc = dialog.findViewById<TextView>(R.id.dialog_fairer_desc_tv)
        val outsideDialog = dialog.findViewById<ConstraintLayout>(R.id.dialog_fairer_outside)

        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window!!.statusBarColor = Color.TRANSPARENT
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        when (type) {
            DialogType.CHANGE -> {
                tvDialogTitle.setText(R.string.add_todo_dialog_title)
                tvDialogDesc.setText(R.string.add_todo_dialog_desc)
            }

            DialogType.DELETE -> {
                tvDialogTitle.setText(R.string.fairer_dialog_delete_title)
                tvDialogDesc.setText(R.string.fairer_dialog_delete_desc)
            }
            DialogType.LOGOUT -> {
                tvDialogTitle.setText(R.string.fairer_dialog_logout_title)
                tvDialogDesc.text = ""
                btnDialogOk.setText(R.string.fairer_dialog_logout_btn_text)
                btnDialogOk.setTextColor(context.getColor(R.color.negative_20))
            }
            DialogType.EXIT -> {
                tvDialogTitle.setText(R.string.fairer_dialog_exit_title)
                tvDialogDesc.setText(R.string.fairer_dialog_exit_desc)
                btnDialogOk.setText(R.string.fairer_dialog_exit_btn_text)
                btnDialogOk.setTextColor(context.getColor(R.color.negative_20))
            }
        }

        outsideDialog.setOnClickListener {
            dialog.dismiss()
        }

        btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDialogOk.setOnClickListener {
            onItemClickListener.onItemClick()
            dialog.dismiss()
        }

        dialog.show()
    }
}

enum class DialogType {
    CHANGE, DELETE, LOGOUT, EXIT
}