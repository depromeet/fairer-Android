package com.depromeet.housekeeper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemBottomSheetAssigneeBinding
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.util.BindingAdapter

class BottomSheetAssigneeAdapter(private val assignees: ArrayList<Assignee>)
    : RecyclerView.Adapter<BottomSheetAssigneeAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener
    var selectedAssignee: ArrayList<Assignee> = arrayListOf() // for multiple choice

    interface MyItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBottomSheetAssigneeBinding = ItemBottomSheetAssigneeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        binding.itemBottomSheetAssigneeCl.isSelected = false
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(assignees[position])

        holder.binding.itemBottomSheetAssigneeCl.apply {
            setOnClickListener {
                // mItemClickListener.onItemClick(position)
                isSelected = !isSelected
            }
        }
    }

    override fun getItemCount(): Int = assignees.size

    inner class ViewHolder(val binding: ItemBottomSheetAssigneeBinding)
        : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("NotifyDataSetChanged")
        fun bind(assignee: Assignee) {
          binding.itemBottomSheetAssigneeNameTv.text = assignee.memberName
          binding.itemBottomSheetAssigneeCheckIv.isSelected = binding.itemBottomSheetAssigneeCl.isSelected
          // BindingAdapter.loadImage(binding.itemBottomSheetAssigneeProfileIv, assignee.profilePath)
        }
    }
}