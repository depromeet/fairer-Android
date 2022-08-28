package com.depromeet.housekeeper.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.databinding.ItemProfileBinding
import com.depromeet.housekeeper.model.AssigneeSelect

class GroupProfileAdapter(
    private val list: MutableList<AssigneeSelect>,
    private val onClick: (AssigneeSelect) -> Unit,
) : RecyclerView.Adapter<GroupProfileAdapter.ItemViewHolder>() {

    fun updateDate(assign: MutableList<AssigneeSelect>) {
        list.clear()
        list.addAll(assign)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ItemViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(assign: AssigneeSelect) {
            binding.assignTemp = assign

            Glide.with(binding.root)
                .load(assign.profilePath)
                .into(binding.ivIcon)

            binding.root.setOnClickListener {
                onClick.invoke(assign)
            }
        }
    }
}