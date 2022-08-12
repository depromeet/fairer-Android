package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemSectionRecyclerviewBinding
import com.depromeet.housekeeper.model.SectionHouseWorks
import com.depromeet.housekeeper.util.SwipeHelperCallback
import com.depromeet.housekeeper.util.VerticalItemDecorator

class MainSectionAdapter(
    private val list: MutableList<SectionHouseWorks>,
    private var houseWorkAdapter: HouseWorkAdapter? = null
) : RecyclerView.Adapter<MainSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainSectionAdapter.ViewHolder {
        val binding: ItemSectionRecyclerviewBinding = ItemSectionRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainSectionAdapter.ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: ItemSectionRecyclerviewBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionHouseWorks: SectionHouseWorks) {
            if (sectionHouseWorks.title != null) {
                binding.tvTitle.visibility = View.VISIBLE
            }
            val string = String.format("남은 집안일 %d" , sectionHouseWorks.list.size)
            binding.tvTitle.text = string
            houseWorkAdapter?.updateDate(sectionHouseWorks.list)
            binding.rvHouseWork.adapter = houseWorkAdapter
            binding.rvHouseWork.addItemDecoration(VerticalItemDecorator(10))
            val swipeHelperCallback = SwipeHelperCallback(houseWorkAdapter!!).apply {
                setClamp(210f)// 스와이프한 뒤 고정시킬 위치 지정
            }
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvHouseWork)
            binding.rvHouseWork.setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(binding.rvHouseWork)
                false
            }
        }
    }
}