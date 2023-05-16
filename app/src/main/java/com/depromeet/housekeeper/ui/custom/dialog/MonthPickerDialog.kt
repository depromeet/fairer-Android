package com.depromeet.housekeeper.ui.custom.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.DialogCalendarBinding
import java.util.Calendar

class MonthPickerDialog(val onClickOk: ()-> Unit) : DialogFragment(){
    private var _binding: DialogCalendarBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val start = Calendar.getInstance() // todo 임시
        start.add(Calendar.YEAR, -1) // 임시
        val now = Calendar.getInstance()
        binding.monthPicker.setDisplayedValue(start, now)

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            onClickOk()
        }
    }

}