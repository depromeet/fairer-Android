package com.depromeet.housekeeper.ui.add

import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.model.request.EditChore
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.request.WeekDays

interface RepeatDate {
    fun getRepeatDays(selectedDays: Array<Boolean>): List<WeekDays>
    fun getRepeatDaysList(type: String, selectedDayList: MutableList<WeekDays>): List<String>
    fun getRepeatDaysString(list: List<String>, end: String?=""): String
    fun updateRepeatInform(cycle: RepeatCycle, chore: Chore, pattern: String): Chore
    fun updateRepeatInform(cycle: RepeatCycle, editChore: EditChore, pattern: String): EditChore
}