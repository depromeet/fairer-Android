package com.depromeet.housekeeper.ui.addHousework.selectTime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemTodoRepeatDayBtnBinding
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.request.WeekDays

class DayRepeatAdapter(private val days: Array<String>) :
    RecyclerView.Adapter<DayRepeatAdapter.ViewHolder>() {

    private var selectedDays = Array(7) { false }
    private lateinit var mItemClickListener: DayItemClickListener

    interface DayItemClickListener {
        fun onItemClick(selectedDays: Array<Boolean>)
    }

    fun setDayItemClickListener(listener: DayItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayRepeatAdapter.ViewHolder {
        val binding: ItemTodoRepeatDayBtnBinding = ItemTodoRepeatDayBtnBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayRepeatAdapter.ViewHolder, position: Int) {
        holder.bind(days[position], selectedDays[position])
    }

    override fun getItemCount(): Int = days.size

    inner class ViewHolder(private val binding: ItemTodoRepeatDayBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String, isSelected: Boolean) {
            binding.itemDayRepeatBtn.text = day
            binding.itemDayRepeatBtn.isSelected = isSelected
            binding.itemDayRepeatBtn.setOnClickListener {
                binding.itemDayRepeatBtn.isSelected = !binding.itemDayRepeatBtn.isSelected
                selectedDays[adapterPosition] = binding.itemDayRepeatBtn.isSelected
                mItemClickListener.onItemClick(selectedDays)
            }
        }
    }

    fun updateSelectedDays(repeatCycle: RepeatCycle) {
        if (repeatCycle == RepeatCycle.DAYILY) {
            for (i in 0..7) { selectedDays[i] = true }
        }
    }

    fun updateSelectedDays(repeatPattern: List<WeekDays>) {
        repeatPattern.forEach {
            var idx = -1
            when (it){
                WeekDays.MON -> idx = 0
                WeekDays.TUE -> idx = 1
                WeekDays.WED -> idx = 2
                WeekDays.THR -> idx = 3
                WeekDays.FRI -> idx = 4
                WeekDays.SAT -> idx = 5
                WeekDays.SUN -> idx = 6
            }
            if (idx != -1) selectedDays[idx] = true
        }
    }

}