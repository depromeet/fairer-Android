package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.LayoutProfileImageviewMiniBinding
import com.depromeet.housekeeper.ui.SignProfileFragment

class SignProfileAdapter(private val profiles: List<SignProfileFragment.ProfileState>): RecyclerView.Adapter<SignProfileAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignProfileAdapter.ViewHolder {
        val binding: LayoutProfileImageviewMiniBinding = LayoutProfileImageviewMiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignProfileAdapter.ViewHolder, position: Int) {
        return holder.bind(profiles[position])
    }

    override fun getItemCount(): Int = profiles.size

  fun clearState(index : Int) {
    profiles.map { it.state = false }
    profiles[index].state = true
    notifyDataSetChanged()
  }

    inner class ViewHolder(private val binding: LayoutProfileImageviewMiniBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(profileData: SignProfileFragment.ProfileState) {
            binding.imgUrl = profileData.url
            binding.signProfileImageview.isSelected = profileData.state
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                binding.signProfileImageview.setOnClickListener {
                    clearState(pos)
                    itemClickListener?.onClick(itemView,profileData.url,pos)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, imgUrl:String, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}