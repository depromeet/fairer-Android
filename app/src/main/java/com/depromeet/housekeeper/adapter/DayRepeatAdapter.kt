package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemTodoRepeatDayBtnBinding

class DayRepeatAdapter(private val days:Array<String>): RecyclerView.Adapter<DayRepeatAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayRepeatAdapter.ViewHolder {
        val binding: ItemTodoRepeatDayBtnBinding = ItemTodoRepeatDayBtnBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayRepeatAdapter.ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    inner class ViewHolder(private val binding: ItemTodoRepeatDayBtnBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String) {
            binding.itemDayRepeatBtn.text = day

            binding.itemDayRepeatBtn.setOnClickListener {
                binding.itemDayRepeatBtn.isSelected = !binding.itemDayRepeatBtn.isSelected
            }
        }
    }
}