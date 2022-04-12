package com.depromeet.housekeeper.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.AddTodoData
import com.depromeet.housekeeper.MainListData
import com.depromeet.housekeeper.databinding.ItemRecyclerAddTodoListBinding


class AddTodoAdapter(private val context: Context) : RecyclerView.Adapter<AddTodoAdapter.ViewHolder>() {
    var datas = mutableListOf<AddTodoData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTodoAdapter.ViewHolder {
        val binding = ItemRecyclerAddTodoListBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddTodoAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }
    class ViewHolder(val binding: ItemRecyclerAddTodoListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : AddTodoData){
            binding.addtodolist = data
        }
    }
}