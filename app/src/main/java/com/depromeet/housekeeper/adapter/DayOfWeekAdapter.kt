package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
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

    private var leftCntMap = mutableMapOf<String, Int>()

    inner class ViewHolder(private val binding: ItemDayOfWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayOfWeek: DayOfWeek) {
            val weekDate = dayOfWeek.date
            val (date, day) = weekDate.split("-")[2] to weekDate.split("-")[3]
            binding.apply {
                isSelect = dayOfWeek.isSelect

                tvChoreCnt.bringToFront()
                val dateKey = weekDate.substring(0,10)
                Timber.d("MAIN : show")
                if (leftCntMap[dateKey] != null){
                    Timber.d("MAIN : show leftCntMap")
                    isChore = true
                    choreCnt = leftCntMap[dateKey]!!
                    ivDots.setImageResource(findDotNums(choreCnt))
                } else {
                    isChore = false
                    choreCnt = 0
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
        Timber.d("MAIN : updateDays : $updateDays")
        list.clear()
        list.addAll(updateDays)
        notifyDataSetChanged()
    }

    fun updateLeftCntMap(leftMap: MutableMap<String, Int>){
        if (leftCntMap != leftMap) {
            leftCntMap = leftMap
            Timber.d("MAIN : updateLeftCntMap : $leftMap")
            notifyDataSetChanged()
        } else {
            Timber.d("MAIN : updateLeftCntMap same :${leftMap}")
        }
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

