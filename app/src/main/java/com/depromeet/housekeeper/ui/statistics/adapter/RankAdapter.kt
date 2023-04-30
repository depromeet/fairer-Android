package com.depromeet.housekeeper.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRankBinding
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember

class RankAdapter(): RecyclerView.Adapter<RankAdapter.ViewHolder>() {
    private var list: List<HouseWorkStatsMember> = listOf()//todo list 입력 뭐 받을지

    inner class ViewHolder(private val binding: ItemRankBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rank: HouseWorkStatsMember){

        }

        @BindingAdapter("android:paddingHorizontal")
        fun setLayoutPaddingHorizontal(view: View, dimen: Float){
            view.setPadding(dimen.toInt(), view.paddingTop, dimen.toInt(), view.paddingBottom)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAdapter.ViewHolder {
        return ViewHolder(ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RankAdapter.ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(rankList: List<HouseWorkStatsMember>){
        this.list = rankList
    }

}