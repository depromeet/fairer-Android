package com.depromeet.housekeeper.ui.custom.timepicker

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.depromeet.housekeeper.util.CalendarUtil.getCalendarKorString
import timber.log.Timber
import java.util.Calendar

class MonthPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    private val monthList = mutableListOf<String>()

    //todo dialog 월 변경하면 그거 저장하고 현재값부터 scroll 해야함

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
        value = month-1
    }

    fun getValueString(): String {
        return monthList[value]
    }
}