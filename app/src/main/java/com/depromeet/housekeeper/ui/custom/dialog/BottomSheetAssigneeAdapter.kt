package com.depromeet.housekeeper.ui.custom.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.databinding.ItemBottomSheetAssigneeBinding
import com.depromeet.housekeeper.model.Assignee

class BottomSheetAssigneeAdapter(
    private val assignees: ArrayList<Assignee>,
    private val curAssignees: ArrayList<Assignee>
) : RecyclerView.Adapter<BottomSheetAssigneeAdapter.ViewHolder>() {

    private var selectedAssignees: ArrayList<Assignee> = arrayListOf() // 선택한 담당자들

    init {
        selectedAssignees.addAll(curAssignees)
    }

    fun getSelectedAssignees(): ArrayList<Assignee> {
        return selectedAssignees
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBottomSheetAssigneeBinding = ItemBottomSheetAssigneeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        binding.itemBottomSheetAssigneeCl.isSelected = false
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(assignees[position])

        holder.binding.itemBottomSheetAssigneeCl.apply {
            setOnClickListener {
                isSelected = !isSelected

                when (isSelected) {
                    true -> selectedAssignees.add(assignees[position])
                    false -> selectedAssignees.remove(assignees[position])
                }
            }
        }

    }

    override fun getItemCount(): Int = assignees.size

    inner class ViewHolder(val binding: ItemBottomSheetAssigneeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(assignee: Assignee) {
            // 초기 selected
            if (selectedAssignees.contains(assignee)) {
                binding.itemBottomSheetAssigneeCheckIv.isSelected = true
                binding.itemBottomSheetAssigneeCl.isSelected = true
            }

            binding.itemBottomSheetAssigneeNameTv.text = assignee.memberName
            binding.itemBottomSheetAssigneeCl.isSelected =
                binding.itemBottomSheetAssigneeCheckIv.isSelected
            Glide.with(binding.root)
                .load(assignee.profilePath)
                .into(binding.itemBottomSheetAssigneeProfileIv)
        }
    }
}