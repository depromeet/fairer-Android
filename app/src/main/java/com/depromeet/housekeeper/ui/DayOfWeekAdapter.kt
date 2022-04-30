package com.depromeet.housekeeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DayOfWeekAdapter(
  private val list: MutableList<Pair<String, String>>,
  private val vm: MainViewModel,
) :
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
      val (date, day) = dayOfWeek
      binding.isSelect = date == SimpleDateFormat(
        "MM-dd",
        Locale.getDefault()).format(Calendar.getInstance().time
      )
      binding.tvNumDay.text = date.split("-")[1]
      binding.tvStrDay.text = day
      binding.layout.setOnClickListener {
        vm.updateSelectDate(date.split("-")[0])
      }
    }
  }

}

