package com.depromeet.housekeeper.ui.addHousework.selectTime.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.databinding.ItemProfileBinding
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.AssigneeSelect

class AddAssigneeAdapter(private val assignees: ArrayList<Assignee>) :
    RecyclerView.Adapter<AddAssigneeAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateAssignees(assignee: ArrayList<Assignee>) {
        assignees.clear()
        assignees.addAll(assignee)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProfileBinding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(assignees[position])
    }

    override fun getItemCount(): Int = assignees.size

    inner class ViewHolder(val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(assignee: Assignee) {
            val assigneeSelect =
                AssigneeSelect(assignee.memberId, assignee.memberName, assignee.profilePath, false)
            binding.assignTemp = assigneeSelect
            Glide.with(binding.root)
                .load(assignee.profilePath)
                .into(binding.ivIcon)
        }
    }
}