package com.depromeet.housekeeper.ui.custom.timepicker

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.depromeet.housekeeper.util.CalendarUtil.getCalendarKorString
import java.util.Calendar

class MonthPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    private val monthList = mutableListOf<String>()

    fun setDisplayedValue(year: Int, month: Int){
        if (monthList.isNotEmpty()) monthList.clear()
        for (i:Int in 0..11){
            val current = Calendar.getInstance().apply {
                this.set(year, i, 1)
            }
            monthList.add(getCalendarKorString(current))
        }

        this.displayedValues = monthList.toTypedArray()
        minValue = 0
        maxValue = monthList.size-1
        value = month
    }

    fun getValueString(): String {
        return monthList[value]
    }
}