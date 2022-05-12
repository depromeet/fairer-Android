package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemHouseworkBinding
import com.depromeet.housekeeper.databinding.ItemNowBinding
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.util.spaceNameMapper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HouseWorkAdapter(
  private val list: MutableList<HouseWork>,
  private val onClick: (HouseWork) -> Unit,
  private val onDone: (HouseWork) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      NOW -> {
        NowViewHolder(
          ItemNowBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
          ))

      }
      else ->
        ItemViewHolder(
          ItemHouseworkBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
          )
        )
    }

  }

  override fun getItemViewType(position: Int): Int {
    return list[position].now
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (list[position].now) {
      ITEM -> {
        (holder as ItemViewHolder).bind(list[position])
      }
      else -> (holder as NowViewHolder).bind()
    }
  }


  override fun getItemCount(): Int = list.size

  private val datePattern = "HH:MM"
  private val format = SimpleDateFormat(datePattern, Locale.getDefault())

  inner class ItemViewHolder(private val binding: ItemHouseworkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(houseWork: HouseWork) {

      binding.isOver = when {
        houseWork.success -> false
        houseWork.scheduledTime == null -> false
        else -> houseWork.scheduledTime < format.format(Calendar.getInstance().time)
      }
      binding.success = houseWork.success
      binding.tvMainTitle.text = houseWork.houseWorkName
      binding.tvMainTime.text = getTime(houseWork)
      binding.tvMainArea.text = spaceNameMapper(houseWork.space)

      binding.root.setOnClickListener {
        onClick.invoke(houseWork)
      }

      binding.btDone.setOnClickListener {
        onDone.invoke(houseWork)
      }
    }
  }

  inner class NowViewHolder(binding: ItemNowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {}
  }

  companion object {
    const val ITEM = 0
    const val NOW = 1
  }


}