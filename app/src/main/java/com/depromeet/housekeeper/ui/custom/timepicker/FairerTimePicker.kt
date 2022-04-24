package com.depromeet.housekeeper.ui.custom.timepicker

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.core.math.MathUtils
import com.depromeet.housekeeper.R
import timber.log.Timber

class FairerTimePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TimePicker(context, attrs) {

    private val defaultInterval = 15
    private val minInterval = 1
    private val maxInterval = 30

    private var timeInterval = defaultInterval
        set(value) {
            if (field !in minInterval..maxInterval) {
                Timber.d("timeInterval must be between $minInterval..$maxInterval")
            }

            field = MathUtils.clamp(minInterval, maxInterval, value)
            setInterval(field)
            invalidate()
        }

    init {
        setInterval()
    }

    @SuppressLint("PrivateApi")
    fun setInterval(
        // @IntRange(from = 1, to = 30)
        timeInterval: Int = defaultInterval
    ) {
        try {
            val classForId = Class.forName("com.android.internal.R\$id")
            val fieldId = classForId.getField("minute").getInt(null)
            (this.findViewById(fieldId) as NumberPicker).apply {
                minValue = resources.getInteger(R.integer.minutes_min)
                maxValue = resources.getInteger(R.integer.minutes_max) / timeInterval - 1
                displayedValues = getDisplayedValue()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDisplayedMinutes(): Int = minute * timeInterval

    private fun getDisplayedValue(
        interval: Int = timeInterval
    ): Array<String> {
        val minutesArray = ArrayList<String>()
        for (i in 0 until resources.getInteger(R.integer.minutes_max) step interval) {
            // minutesArray.add(i.toString())
            minutesArray.add(String.format("%02d", i))
        }

        return minutesArray.toArray(arrayOf(""))
    }

}