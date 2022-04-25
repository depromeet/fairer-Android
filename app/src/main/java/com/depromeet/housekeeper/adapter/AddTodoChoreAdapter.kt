package com.depromeet.housekeeper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRecyclerAddTodoListBinding

class AddTodoChoreAdapter(private val chores: ArrayList<String>)
    : RecyclerView.Adapter<AddTodoChoreAdapter.ViewHolder>() {

    // for single choice
    private var selectedChore: ArrayList<Int> = arrayListOf()

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
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeChore(position: Int) {
        chores.removeAt(position)
        if(position == chores.size && selectedChore[position] == 1) {
            // 마지막 아이템 선택 상태에서 삭제하면 이전 포지션으로 포커스 넘어가게
            // default : 다음 포지션으로 포커스
            selectedChore.removeAt(position)
            selectedChore[position - 1] = 1
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerAddTodoListBinding = ItemRecyclerAddTodoListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(chores[position])
    }

    override fun getItemCount(): Int = chores.size

    inner class ViewHolder(private val binding: ItemRecyclerAddTodoListBinding)
        : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(chore: String) {
            binding.itemAddTodoNameTv.text = chore

            binding.itemAddTodoLayout.apply {
                isSelected = selectedChore[adapterPosition] == 1
                setOnClickListener {
                    for(k in selectedChore.indices) {
                        if(k == adapterPosition) {
                            selectedChore[k] = 1
                        }
                        else {
                            selectedChore[k] = 0
                        }
                    }
                    notifyDataSetChanged()
                }
            }

            binding.itemAddTodoDeleteIv.setOnClickListener {
                removeChore(adapterPosition)
            }
        }
    }
}