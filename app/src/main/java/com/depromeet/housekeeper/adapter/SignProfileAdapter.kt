package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.LayoutProfileImageviewMiniBinding

class SignProfileAdapter(private val imgUrls: List<String>): RecyclerView.Adapter<SignProfileAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignProfileAdapter.ViewHolder {
        val binding: LayoutProfileImageviewMiniBinding = LayoutProfileImageviewMiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignProfileAdapter.ViewHolder, position: Int) {
        return holder.bind(imgUrls[position])
    }

    override fun getItemCount(): Int = imgUrls.size

    inner class ViewHolder(private val binding: LayoutProfileImageviewMiniBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(imgUrl:String){
            binding.imgUrl = imgUrl
            val pos = adapterPosition
            binding.position = pos
            if(pos!= RecyclerView.NO_POSITION)
            {
                binding.signProfileImageview.setOnClickListener {
                    itemClickListener?.onClick(itemView,imgUrl,pos)
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