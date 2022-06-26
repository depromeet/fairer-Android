package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemSmallProfileBinding
import com.depromeet.housekeeper.model.Assignee

class SmallProfileAdapter(
  private val list: MutableList<Assignee>,
) : RecyclerView.Adapter<SmallProfileAdapter.ItemViewHolder>() {

  fun updateDate(assignee: MutableList<Assignee>) {
    list.clear()
    list.addAll(assignee)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      ItemSmallProfileBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  inner class ItemViewHolder(private val binding: ItemSmallProfileBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(assignee: Assignee) {
      binding.imageUrl = assignee.profilePath
    }
  }
}