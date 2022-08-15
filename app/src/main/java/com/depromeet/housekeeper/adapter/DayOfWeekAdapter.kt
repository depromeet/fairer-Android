package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemDayOfWeekBinding
import com.depromeet.housekeeper.model.DayOfWeek
import timber.log.Timber

class DayOfWeekAdapter(
    private val list: MutableList<DayOfWeek>,
    private val onClick: (DayOfWeek) -> Unit,
) :
    RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder>() {
    //todo 삭제 예정
    private var choreSize: Int? = null

    private var leftCntMap = mutableMapOf<String, Int>()

    inner class ViewHolder(private val binding: ItemDayOfWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayOfWeek: DayOfWeek) {
            val weekDate = dayOfWeek.date
            val (date, day) = weekDate.split("-")[2] to weekDate.split("-")[3]
            binding.apply {
                isSelect = dayOfWeek.isSelect

                // todo 삭제 예정
                if (choreSize != null && choreSize != 0) {
                    isChore = true
                    tvChoreCnt.bringToFront()
                    tvChoreCnt.text = choreSize.toString()
                    ivDots.setImageResource(findDotNums(choreSize!!))
                    choreSize = null
                }

                tvChoreCnt.bringToFront()
                val dateKey = weekDate.substring(0,10)
                if (leftCntMap[dateKey] != null){
                    choreCnt = leftCntMap[dateKey]!!
                    ivDots.setImageResource(findDotNums(choreCnt))
                }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDayOfWeekBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun updateDate(updateDays: MutableList<DayOfWeek>) {
        list.clear()
        list.addAll(updateDays)
        notifyDataSetChanged()
    }

    fun updateChoreSize(chores: Int) {
        choreSize = chores
        val index = list.indexOfFirst { it.isSelect }
        notifyItemChanged(index)
    }

    fun updateLeftCntMap(leftMap: MutableMap<String, Int>){
        leftCntMap = leftMap
        notifyDataSetChanged()
    }

    private fun updateLastSelectView() {
        val index = list.indexOfFirst { it.isSelect }
        if (index != -1) {
            list[index].isSelect = false
        }
        notifyItemChanged(index)
    }


    private fun findDotNums(cnt: Int): Int {
        return if (cnt in 1 .. 3) {
            R.drawable.ic_dot_one
        } else if (cnt in 4..6) {
            R.drawable.ic_dots_two
        }else {
            R.drawable.ic_dots_three
        }
    }
}

