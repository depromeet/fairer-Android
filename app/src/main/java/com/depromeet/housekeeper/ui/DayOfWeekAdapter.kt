package com.depromeet.housekeeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding
import com.depromeet.housekeeper.model.DayOfWeek

class DayOfWeekAdapter(
  private val list: MutableList<DayOfWeek>,
  private val onClick: (DayOfWeek) -> Unit,
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
      binding.apply {
        isSelect = dayOfWeek.isSelect
        tvNumDay.text = date
        tvStrDay.text = day
        layout.setOnClickListener {
          updateLastSelectView()
          dayOfWeek.isSelect = !dayOfWeek.isSelect
          binding.isSelect = dayOfWeek.isSelect
          onClick.invoke(dayOfWeek)
        }
      }
    }
  }

  private fun updateLastSelectView() {
    val index = list.indexOfFirst { it.isSelect }
    if (index != -1) {
      list[index].isSelect = false
    }
    notifyItemChanged(index)
  }
}

