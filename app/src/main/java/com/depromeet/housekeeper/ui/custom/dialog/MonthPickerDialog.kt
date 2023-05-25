package com.depromeet.housekeeper.ui.custom.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.DialogCalendarBinding
import timber.log.Timber
import java.util.*

class MonthPickerDialog(val currentDate: Date, val onClickOk: (date: Date)-> Unit) : DialogFragment(){
    private var _binding: DialogCalendarBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_r12)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance().apply {
            this.time = currentDate
        }
        binding.monthPicker.setDisplayedValue(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))

        binding.btnCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.btnOk.setOnClickListener {
            dialog!!.dismiss()
            val yearMonth: String = binding.monthPicker.getValueString()

            val date = Date()
            date.year = yearMonth.substring(0,4).toInt() - 1900
            Timber.d("yearMonth : ${yearMonth.substring(6, yearMonth.length-1)}")
            date.month = yearMonth.substring(6, yearMonth.length-1).toInt() -1

            onClickOk(date)
        }
    }

}