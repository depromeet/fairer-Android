package com.depromeet.housekeeper.ui.custom.timepicker

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.depromeet.housekeeper.util.CalendarUtil.getCalendarKorString
import timber.log.Timber
import java.util.Calendar

class MonthPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {

    init {
        val start = Calendar.getInstance() // todo 임시
        start.add(Calendar.YEAR, -1) // 임시
        val end = Calendar.getInstance()
        setDisplayedValue(start, end)
    }

    fun setDisplayedValue(start: Calendar, end: Calendar){
        val monthList = mutableListOf<String>()
        val current: Calendar = start
        while (current <= end){
            monthList.add(getCalendarKorString(current))
            current.add(Calendar.MONTH, 1)
        }

        this.displayedValues = monthList.toTypedArray()
        Timber.d("$monthList")
        minValue = 0
        maxValue = monthList.size-1
        value = monthList.size-1
    }
}