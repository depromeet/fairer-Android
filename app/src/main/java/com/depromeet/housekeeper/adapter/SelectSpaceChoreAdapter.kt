package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemSelectSpaceTaskBinding

class SelectSpaceChoreAdapter(private val chores: List<String>):RecyclerView.Adapter<SelectSpaceChoreAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View,chore:String,position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectSpaceChoreAdapter.ViewHolder {
        val binding:ItemSelectSpaceTaskBinding = ItemSelectSpaceTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectSpaceChoreAdapter.ViewHolder, position: Int) {
        return holder.bind(chores[position])
    }

    override fun getItemCount(): Int =chores.size

    inner class ViewHolder(private val binding:ItemSelectSpaceTaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chore:String){
            binding.selectSpaceBtnTask.text=chore
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                binding.selectSpaceBtnTask.setOnClickListener {
                    itemClickListener?.onClick(itemView,chore,pos)
                }
            }
        }
    }


}