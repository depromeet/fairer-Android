package com.depromeet.housekeeper.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.databinding.ItemBottomSheetFeedbackBinding
import com.depromeet.housekeeper.model.response.FeedbackFindOneResponseDto

class FeedbackAdapter(
    private val list: MutableList<FeedbackFindOneResponseDto>
) : RecyclerView.Adapter<FeedbackAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemBottomSheetFeedbackBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemBottomSheetFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedbackFindOneResponseDto) {
            Glide.with(binding.root)
                .load(item.profilePath)
                .into(binding.itemBottomSheetAssigneeProfileIv)
            binding.itemBottomSheetAssigneeNameTv.text = item.memberName
            binding.emojiNum = item.emoji
            binding.tvFeedbackText.text = item.comment
        }
    }

    fun updateFeedbackList(newList: List<FeedbackFindOneResponseDto>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}