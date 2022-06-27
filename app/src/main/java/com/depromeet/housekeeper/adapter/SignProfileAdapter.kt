package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.LayoutProfileImageviewMiniBinding
import com.depromeet.housekeeper.ui.SignProfileViewModel
import timber.log.Timber

class SignProfileAdapter(private val profiles: List<SignProfileViewModel.ProfileState>) :
    RecyclerView.Adapter<SignProfileAdapter.ViewHolder>() {
    var selectedPosition: Int = 99
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignProfileAdapter.ViewHolder {
        val binding: LayoutProfileImageviewMiniBinding = LayoutProfileImageviewMiniBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignProfileAdapter.ViewHolder, position: Int) {
        return holder.bind(profiles[position])
    }

    override fun getItemCount(): Int = profiles.size

    fun clearState(index: Int) {
        profiles.map { it.state = false }
        profiles[index].state = true
        notifyItemChanged(index)
        if(selectedPosition==99){
            selectedPosition = index
        }
        else{
            notifyItemChanged(selectedPosition)
            selectedPosition = index
        }
        Timber.d("$selectedPosition")
    }

    inner class ViewHolder(private val binding: LayoutProfileImageviewMiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profileData: SignProfileViewModel.ProfileState) {
            binding.imgUrl = profileData.url
            binding.signProfileImageview.isSelected = profileData.state
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                binding.signProfileImageview.setOnClickListener {
                    clearState(pos)
                    itemClickListener?.onClick(itemView, profileData.url, pos)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, imgUrl: String, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}