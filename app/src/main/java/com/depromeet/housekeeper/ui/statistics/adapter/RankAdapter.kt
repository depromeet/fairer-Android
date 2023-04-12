package com.depromeet.housekeeper.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRankBinding

class RankAdapter(
    private val list: Array<Int> //todo list 입력 뭐 받을지
): RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRankBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rank: Int){

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

}