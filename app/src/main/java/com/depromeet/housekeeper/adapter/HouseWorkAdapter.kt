package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemHouseworkBinding
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.util.spaceNameMapper
import java.text.SimpleDateFormat
import java.util.*

class HouseWorkAdapter(
  private val list: MutableList<HouseWork>,
  private val onClick: (HouseWork) -> Unit,
  private val onDone: (HouseWork) -> Unit,
) : RecyclerView.Adapter<HouseWorkAdapter.ItemViewHolder>() {

  private fun getTime(houseWork: HouseWork): String {
    houseWork.scheduledTime?.let {
      val hour = houseWork.scheduledTime.split(":")[0].toInt()
      val min = houseWork.scheduledTime.split(":")[1]
      return when {
        hour > 12 -> "오후 ${hour % 12}:${min}"
        else -> "오전 ${hour}:${min}"
      }
    } ?: return "하루 종일"
  }

  fun updateDate(houseWork: MutableList<HouseWork>) {
    list.clear()
    list.addAll(houseWork)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      ItemHouseworkBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  }

  override fun getItemViewType(position: Int): Int {
    return list[position].now
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  private val datePattern = "HH:MM"
  private val format = SimpleDateFormat(datePattern, Locale.getDefault())
  private val remoteDatePattern = "YYYY-MM-dd"
  private val remoteFormat = SimpleDateFormat(remoteDatePattern, Locale.getDefault())

  inner class ItemViewHolder(private val binding: ItemHouseworkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(houseWork: HouseWork) {

      binding.isOver = when {
        houseWork.success -> false
        houseWork.scheduledTime == null -> false
        else -> if (remoteFormat.format(Calendar.getInstance().time) == houseWork.scheduledDate) {
          houseWork.scheduledTime > format.format(Calendar.getInstance().time)
        } else {
          false
        }
      }
      binding.success = houseWork.success
      binding.tvMainTitle.text = houseWork.houseWorkName
      binding.tvMainTime.text = getTime(houseWork)
      binding.tvMainArea.text = spaceNameMapper(houseWork.space)

      binding.root.setOnClickListener {
        onClick.invoke(houseWork)
      }

      /*binding.btDone.setOnClickListener {
        onDone.invoke(houseWork)
      }*/
      val adapter = SmallProfileAdapter(houseWork.assignees.toMutableList())
      binding.rvProfileAdapter.adapter = adapter

    }
  }
}