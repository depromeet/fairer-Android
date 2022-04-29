package com.depromeet.housekeeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding
import java.util.Calendar

class DayOfWeekAdapter(private val list: MutableList<Pair<String, String>>) :
  RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder>() {

  fun updateDate(updateDays: MutableList<Pair<String, String>>) {
    list.clear()
    list.addAll(updateDays)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemDayOfWeekBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  inner class ViewHolder(private val binding: ItemDayOfWeekBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dayOfWeek: Pair<String, String>) {
      val (number, day) = dayOfWeek
      binding.isSelect = number.toInt() == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
      binding.tvNumDay.text = number.toString()
      binding.tvStrDay.text = day
      binding.layout.setOnClickListener {
        //TODO ClickListener 처리
      }
    }
  }

}

