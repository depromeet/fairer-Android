package com.depromeet.housekeeper.ui.custom.timepicker

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.depromeet.housekeeper.util.CalendarUtil.getCalendarString
import java.util.Calendar

class MonthPicker(context: Context) : NumberPicker(context) {

    init {
        val start = Calendar.getInstance() // todo 임시
        start.add(Calendar.YEAR, -1) // 임시
        val end = Calendar.getInstance()
        setDisplayedValue(start, end)
    }

    fun setDisplayedValue(start: Calendar, end: Calendar){
        val monthList = mutableListOf<String>()
        val current: Calendar = end
        while (current >= start){
            monthList.add(getCalendarString(current))
            current.add(Calendar.MONTH, -1)
        }

        this.displayedValues = monthList.toTypedArray()
    }
}