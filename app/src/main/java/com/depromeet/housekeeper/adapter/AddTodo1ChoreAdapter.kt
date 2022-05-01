package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemAddTodo1TaskBinding

class AddTodo1ChoreAdapter(private val chores: ArrayList<String>):RecyclerView.Adapter<AddTodo1ChoreAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddTodo1ChoreAdapter.ViewHolder {

        val binding:ItemAddTodo1TaskBinding = ItemAddTodo1TaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        /*binding.addTodo1BtnTask.setOnClickListener {
            it.isSelected = !it.isSelected

        }*/
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddTodo1ChoreAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        return holder.bind(chores[position])
    }

    override fun getItemCount(): Int =chores.size

    inner class ViewHolder(private val binding:ItemAddTodo1TaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chore:String){
            binding.addTodo1BtnTask.text=chore
        }
    }


}