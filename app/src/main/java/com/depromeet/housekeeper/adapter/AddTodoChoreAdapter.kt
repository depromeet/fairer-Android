package com.depromeet.housekeeper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRecyclerAddTodoListBinding

class AddTodoChoreAdapter(private val list: Array<String>)
    : RecyclerView.Adapter<AddTodoChoreAdapter.ViewHolder>() {

    // for single choice
    private var selectedChore: ArrayList<Int> = arrayListOf()

    init {
        for(i in list) {
            if(list.indexOf(i) == 0) {
                // first item auto focus
                selectedChore.add(1)
            }
            else {
                selectedChore.add(0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerAddTodoListBinding = ItemRecyclerAddTodoListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

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
        }
    }
}