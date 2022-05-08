package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemHouseworkBinding
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.util.spaceNameMapper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HouseWorkAdapter(
  private val list: MutableList<HouseWork>,
  private val onClick: (HouseWork) -> Unit,
) : RecyclerView.Adapter<HouseWorkAdapter.ViewHolder>() {

  private fun getTime(houseWork: HouseWork): String {
    val hour = houseWork.scheduledTime.split(":")[0].toInt()
    val min = houseWork.scheduledTime.split(":")[1]
    return when {
      hour > 12 -> "오후 ${hour % 12}:${min}"
      else -> "오전 ${hour}:${min}"
    }
  }

  fun updateDate(houseWork: MutableList<HouseWork>) {
    list.clear()
    list.addAll(houseWork)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemHouseworkBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  private val datePattern = "HH:MM"
  private val format = SimpleDateFormat(datePattern, Locale.getDefault())

  inner class ViewHolder(private val binding: ItemHouseworkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(houseWork: HouseWork) {

      binding.isOver = houseWork.scheduledTime < format.format(Calendar.getInstance().time)
      binding.tvMainTitle.text = houseWork.houseWorkName
      binding.tvMainTime.text = getTime(houseWork)
      binding.tvMainArea.text = spaceNameMapper(houseWork.space)
    }
  }
}