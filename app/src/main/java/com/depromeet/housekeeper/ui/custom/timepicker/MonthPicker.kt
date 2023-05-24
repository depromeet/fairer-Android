package com.depromeet.housekeeper.ui.custom.timepicker

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.depromeet.housekeeper.util.CalendarUtil.getCalendarKorString
import timber.log.Timber
import java.util.Calendar

class MonthPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    val monthList = mutableListOf<String>()
    init {
        val start = Calendar.getInstance() // todo 임시
        start.add(Calendar.YEAR, -1) // 임시
        val end = Calendar.getInstance()
        setDisplayedValue(start, end)
    }

    //todo dialog 월 변경하면 그거 저장하고 현재값부터 scroll 해야함
    fun setDisplayedValue(start: Calendar, end: Calendar){
        if (monthList.isNotEmpty()) monthList.clear()

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

    fun getValueString(): String {
        return monthList[value]
    }
}