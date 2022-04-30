package com.depromeet.housekeeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding
import com.depromeet.housekeeper.model.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DayOfWeekAdapter(
  private val list: MutableList<DayOfWeek>,
  val vm: MainViewModel,
) :
  RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder>() {

  fun updateDate(updateDays: MutableList<DayOfWeek>) {
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
    fun bind(dayOfWeek: DayOfWeek) {
      val weekDate = dayOfWeek.date
      val (date, day) = weekDate.split("-")[2] to weekDate.split("-")[3]
      binding.isSelect = (dayOfWeek.date == SimpleDateFormat(
        "YYYY-MM-dd-EEE",
        Locale.getDefault()).format(Calendar.getInstance().time
      ) || dayOfWeek.isSelect)
      binding.tvNumDay.text = date
      binding.tvStrDay.text = day
      binding.layout.setOnClickListener {
        vm.updateSelectDate(weekDate.split("-")[1])
      }
    }
  }


}

