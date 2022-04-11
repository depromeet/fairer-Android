package com.depromeet.housekeeper.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.MainListData
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemRecyclerMainListBinding

class MainListAdapter(private val context : Context) : RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    var datas = mutableListOf<MainListData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListAdapter.ViewHolder {
        val binding = ItemRecyclerMainListBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }
    class ViewHolder(val binding: ItemRecyclerMainListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : MainListData){
            binding.mainlist = data
        }
    }
}