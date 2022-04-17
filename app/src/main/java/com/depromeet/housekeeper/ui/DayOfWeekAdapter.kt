package com.depromeet.housekeeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding

class DayOfWeekAdapter(private val list: List<Pair<Int, String>>) :
  RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemDayOfWeekBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false)
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val (number, day) = list[position]
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  inner class ViewHolder(private val binding: ItemDayOfWeekBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dayOfWeek: Pair<Int, String>) {
      val (number, day) = dayOfWeek
      binding.tvNumDay.text = number.toString()
      binding.tvStrDay.text = day
      binding.layout.setOnClickListener {
        //TODO ClickListener 처리
      }
    }
  }

}

