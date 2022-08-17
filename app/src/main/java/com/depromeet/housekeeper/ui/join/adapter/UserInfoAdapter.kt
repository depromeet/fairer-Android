package com.depromeet.housekeeper.ui.join.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemUserProfileBinding
import com.depromeet.housekeeper.model.Assignee

class UserInfoAdapter(private val GroupInfo : ArrayList<Assignee>):RecyclerView.Adapter<UserInfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemUserProfileBinding = ItemUserProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(GroupInfo[position])
    }

    override fun getItemCount(): Int = GroupInfo.size

    inner class ViewHolder(private val binding: ItemUserProfileBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(UserInfo : Assignee){
            binding.imgUrl = UserInfo.profilePath
            binding.userName.text = UserInfo.memberName
        }
    }
}