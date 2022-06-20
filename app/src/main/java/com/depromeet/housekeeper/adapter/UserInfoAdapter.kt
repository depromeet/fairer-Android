package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemUserProfileBinding

class UserInfoAdapter(private val imgUrls: List<String>, private val userName: List<String>):RecyclerView.Adapter<UserInfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserInfoAdapter.ViewHolder {
        val binding: ItemUserProfileBinding = ItemUserProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoAdapter.ViewHolder, position: Int) {
        return holder.bind(imgUrls[position], userName[position])
    }

    override fun getItemCount(): Int = imgUrls.size

    inner class ViewHolder(private val binding: ItemUserProfileBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(imgUrl:String, userName:String){
            binding.imgUrl = imgUrl
        }
    }
}