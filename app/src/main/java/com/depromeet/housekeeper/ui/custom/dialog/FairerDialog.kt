package com.depromeet.housekeeper.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.DialogFairerBinding
import com.depromeet.housekeeper.databinding.DialogLogoutBinding
import com.depromeet.housekeeper.databinding.DialogRepeatBinding
import com.depromeet.housekeeper.model.request.EditType
import timber.log.Timber

class FairerDialog(private val context: Context, private val type: DialogType) {
    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onEditItemClickListener: OnEditItemClickListener
    private val dialog = Dialog(context)
    private val logoutDialog = Dialog(context)
    private val repeatDialog = Dialog(context)

    interface OnItemClickListener {
        fun onItemClick()
    }

    interface OnEditItemClickListener {
        fun onItemClick()
    }

    fun showLogoutDialog() {
        initDialog<DialogLogoutBinding>(logoutDialog, R.layout.dialog_logout)

        val btnDialogCancel =
            logoutDialog.findViewById<AppCompatButton>(R.id.dialog_logout_cancel_btn)
        val btnDialogOk = logoutDialog.findViewById<AppCompatButton>(R.id.dialog_logout_ok_btn)
        val tvDialogTitle = logoutDialog.findViewById<TextView>(R.id.dialog_logout_title_tv)
        val outsideDialog = logoutDialog.findViewById<ConstraintLayout>(R.id.dialog_logout_outside)

        tvDialogTitle.setText(R.string.fairer_dialog_logout_title)
        btnDialogOk.setText(R.string.fairer_dialog_logout_btn_text)
        btnDialogOk.setTextColor(context.getColor(R.color.negative_20))

        outsideDialog.setOnClickListener {
            logoutDialog.dismiss()
        }

        btnDialogCancel.setOnClickListener {
            logoutDialog.dismiss()
        }

        btnDialogOk.setOnClickListener {
            onItemClickListener.onItemClick()
            logoutDialog.dismiss()
        }
        logoutDialog.show()


    }

    fun showDialog() {
        initDialog<DialogFairerBinding>(dialog, R.layout.dialog_fairer)

        val btnDialogCancel = dialog.findViewById<AppCompatButton>(R.id.dialog_fairer_cancel_btn)
        val btnDialogOk = dialog.findViewById<AppCompatButton>(R.id.dialog_fairer_ok_btn)
        val tvDialogTitle = dialog.findViewById<TextView>(R.id.dialog_fairer_title_tv)
        val tvDialogDesc = dialog.findViewById<TextView>(R.id.dialog_fairer_desc_tv)
        val outsideDialog = dialog.findViewById<ConstraintLayout>(R.id.dialog_fairer_outside)


        when (type) {
            DialogType.CHANGE -> {
                tvDialogTitle.setText(R.string.add_todo_dialog_title)
                tvDialogDesc.setText(R.string.add_todo_dialog_desc)
            }

            DialogType.DELETE -> {
                tvDialogTitle.setText(R.string.fairer_dialog_delete_title)
                tvDialogDesc.setText(R.string.fairer_dialog_delete_desc)
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

    fun showRepeatDialog(onOkClick: (type: EditType) -> Unit) {
        val binding = initDialog<DialogRepeatBinding>(repeatDialog, R.layout.dialog_repeat)

        binding.apply {
            binding.radioBtn1.isChecked = true

            if (type == DialogType.DELETE) {
                title = context.getString(R.string.dialog_repeat_title_delete)
                okText = context.getString(R.string.dialog_repeat_delete)
            } else if (type == DialogType.EDIT) {
                title = context.getString(R.string.dialog_repeat_title_modify)
                okText = context.getString(R.string.dialog_repeat_modify)
            }

            dialogRepeatOutside.setOnClickListener {
                repeatDialog.dismiss()
            }

            btnCancel.setOnClickListener {
                repeatDialog.dismiss()
            }

            var checkedType = EditType.ONLY
            radioGroup.setOnCheckedChangeListener { group, i ->
                when (i) {
                    R.id.radio_btn_1 -> checkedType = EditType.ONLY
                    R.id.radio_btn_2 -> checkedType = EditType.FORWARD
                    R.id.radio_btn_3 -> checkedType = EditType.ALL
                }
            }

            btnOk.setOnClickListener {
                onOkClick(checkedType)
                repeatDialog.dismiss()
            }
        }

        repeatDialog.show()

    }

    private fun <T : ViewDataBinding> initDialog(dialog: Dialog, resId: Int): T {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val binding = DataBindingUtil.inflate<T>(LayoutInflater.from(context), resId, null, false)
        dialog.setContentView(binding.root)

        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window!!.statusBarColor = Color.TRANSPARENT
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        return binding
    }

}

enum class DialogType {
    CHANGE, DELETE, EDIT, LOGOUT, EXIT
}