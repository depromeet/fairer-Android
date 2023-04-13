package com.depromeet.housekeeper.ui.main.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.DialogFeedbackReturnBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReturnFeedbackDialog(
    private val onUrgeClick: () -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFeedbackReturnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFeedbackReturnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(24.dpToPx, 0, 24.dpToPx, 0)
                it.layoutParams = layoutParams
            }

        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDialogFeedbackUrge.setOnClickListener {
            onUrgeClick.invoke()
            dismiss()
        }

        binding.btnDialogFeedbackUrgeCancel.setOnClickListener {
            dismiss()
        }
    }

    private val Int.dpToPx: Int
        get() = (this * resources.displayMetrics.density).toInt()
}