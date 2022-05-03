package com.depromeet.housekeeper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRecyclerAddTodoListBinding
import com.depromeet.housekeeper.model.Chore
import timber.log.Timber

class AddTodoChoreAdapter(private val chores: ArrayList<Chore>)
    : RecyclerView.Adapter<AddTodoChoreAdapter.ViewHolder>() {

    private var selectedChore: ArrayList<Int> = arrayListOf() // for single choice
    private lateinit var mItemClickListener: MyItemClickListener

    interface MyItemClickListener {
        fun onItemClick(position: Int)
    }


    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    init {
        for(i in chores) {
            if(chores.indexOf(i) == 0) {
                // first item auto focus
                selectedChore.add(1)
            }
            else {
                selectedChore.add(0)
            }
        }
        Timber.d(selectedChore.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeChore(position: Int) {
        chores.removeAt(position)
        if(position == chores.size && selectedChore[position] == 1) {
            // 마지막 아이템 선택 상태에서 삭제하면 이전 포지션으로 포커스 넘어가게
            // default : 다음 포지션으로 포커스
            selectedChore[position - 1] = 1
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerAddTodoListBinding = ItemRecyclerAddTodoListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chores[position])

        holder.binding.itemAddTodoLayout.apply {

            isSelected = selectedChore[position] == 1
            setOnClickListener {
                mItemClickListener.onItemClick(position)

                for(k in selectedChore.indices) {
                    if(k == position) {
                        selectedChore[k] = 1
                    }
                    else {
                        selectedChore[k] = 0
                    }
                }
                notifyDataSetChanged()
            }
        }

        holder.binding.itemAddTodoDeleteIv.setOnClickListener {
            if(chores.size > 1) {
                removeChore(position)
            }
        }
    }

    override fun getItemCount(): Int = chores.size

    inner class ViewHolder(val binding: ItemRecyclerAddTodoListBinding)
        : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(chore: Chore) {
            if(chore.scheduleTime == "하루 종일") {
                binding.itemAddTodoTimeTv.text = chore.scheduleTime
            }
            else {
                binding.itemAddTodoTimeTv.text = parseTime(chore.scheduleTime)
            }
            binding.itemAddTodoNameTv.text = chore.houseWorkName
        }

        private fun parseTime(time: String): String {
            val temp = time.split(":")
            val hour = temp[0].toInt()
            val min = temp[1].toInt()

            return if(hour <= 12) {
                "오전\n${String.format("%02d", hour)}:${String.format("%02d", min)}"
            } else {
                "오후\n${String.format("%02d", hour - 12)}:${String.format("%02d", min)}"
            }
        }
    }
}