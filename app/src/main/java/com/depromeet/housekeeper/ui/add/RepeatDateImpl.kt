package com.depromeet.housekeeper.ui.add

import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.model.request.EditChore
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.request.WeekDays

class RepeatDateImpl : RepeatDate {
    override fun getRepeatDays(selectedDays: Array<Boolean>): List<WeekDays> {
        val dayList = mutableListOf<WeekDays>()
        for (i in selectedDays.indices) {
            if (!selectedDays[i]) continue
            WeekDays.values().find { value ->
                value.ordinal == i
            }?.let { day ->
                dayList.add(day)
            }

        }

        return dayList.toList()
    }

    override fun getRepeatDaysList(
        type: String,
        selectedDayList: MutableList<WeekDays>
    ): List<String> {
        return selectedDayList.map { if (type == "kor") it.kor else it.eng }.toList()
    }

    override fun getRepeatDaysString(list: List<String>, end: String?): String =
        list.joinToString(",") + end


    override fun updateRepeatInform(cycle: RepeatCycle, chore: Chore, pattern: String): Chore {
        chore.repeatCycle = cycle.value
        chore.repeatPattern = pattern
        return chore
    }

    override fun updateRepeatInform(
        cycle: RepeatCycle,
        editChore: EditChore,
        pattern: String
    ): EditChore {
        editChore.repeatCycle = cycle.value
        editChore.repeatPattern = pattern
        return editChore
    }
}