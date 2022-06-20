package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemProfileBinding
import com.depromeet.housekeeper.model.AssigneeTemp

class GroupProfileAdapter(
  private val list: MutableList<AssigneeTemp>,
  private val onClick: (AssigneeTemp) -> Unit,
) : RecyclerView.Adapter<GroupProfileAdapter.ItemViewHolder>() {

  fun updateDate(assign: MutableList<AssigneeTemp>) {
    list.clear()
    list.addAll(assign)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      ItemProfileBinding.inflate(LayoutInflater.from(parent.context),
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
    fun bind(assign: AssigneeTemp) {
      binding.assignTemp = assign
    }
  }
}