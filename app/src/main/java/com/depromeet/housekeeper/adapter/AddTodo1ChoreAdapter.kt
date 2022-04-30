package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemAddTodo1TaskBinding

class AddTodo1ChoreAdapter(private val chores: ArrayList<String>):RecyclerView.Adapter<AddTodo1ChoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddTodo1ChoreAdapter.ViewHolder {
        val binding:ItemAddTodo1TaskBinding = ItemAddTodo1TaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddTodo1ChoreAdapter.ViewHolder, position: Int) {
        return holder.bind(chores[position])
    }

    override fun getItemCount(): Int =chores.size


    inner class ViewHolder(private val binding:ItemAddTodo1TaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chore:String){
            binding.addTodo1BtnTask.text=chore
        }
    }
}