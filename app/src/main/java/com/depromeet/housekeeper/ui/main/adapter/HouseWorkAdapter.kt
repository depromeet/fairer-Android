package com.depromeet.housekeeper.ui.main.adapter

import  android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemHouseworkBinding
import com.depromeet.housekeeper.model.FeedbackHouseworkResponse
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.util.spaceNameMapper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HouseWorkAdapter(
    private val list: MutableList<HouseWork>,
    private val onClick: (HouseWork) -> Unit,
    private val onDone: (HouseWork, Boolean) -> Unit,
    private val onLongClick: (View, Boolean, Boolean, Boolean, HouseWork) -> Unit,
    private val feedbackClick: (Int?) -> Unit
) : RecyclerView.Adapter<HouseWorkAdapter.ItemViewHolder>() {
    private var doneHouseWorks: MutableList<HouseWork> = mutableListOf()


    private fun getTime(houseWork: HouseWork): String {
        houseWork.scheduledTime?.let {
            val hour = houseWork.scheduledTime.split(":")[0].toInt()
            val min = houseWork.scheduledTime.split(":")[1]
            return when {
                hour > 12 -> "오후 ${hour % 12}:${min}"
                else -> "오전 ${hour}:${min}"
            }
        } ?: return "하루 종일"
    }

    fun updateDate(remainHouseWork: MutableList<HouseWork>, doneHouseWork: MutableList<HouseWork>) {
        list.clear()
        list.addAll(remainHouseWork)
        list.addAll(doneHouseWork)
        doneHouseWorks = doneHouseWork
        notifyDataSetChanged()
    }

    fun callDone(layoutPosition: Int) {
        onDone.invoke(
            list[layoutPosition],
            isEmojiEmpty(list[layoutPosition].feedbackHouseworkResponse!!)
        )
        notifyItemChanged(layoutPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemHouseworkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

//    override fun getItemViewType(position: Int): Int {
//        return list[position].now
//    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    private val datePattern = "HH:MM"
    private val format = SimpleDateFormat(datePattern, Locale.getDefault())
    private val remoteDatePattern = "YYYY-MM-dd"
    private val remoteFormat = SimpleDateFormat(remoteDatePattern, Locale.getDefault())

    inner class ItemViewHolder(private val binding: ItemHouseworkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(houseWork: HouseWork) {
            if (doneHouseWorks.isNotEmpty()) {
                if (doneHouseWorks.first() == houseWork) {
                    binding.tvTitle.text = String.format("끝낸 집안일 %d", doneHouseWorks.size)
                    binding.tvTitle.visibility = View.VISIBLE
                } else binding.tvTitle.visibility = View.GONE
            } else binding.tvTitle.visibility = View.GONE
            binding.isOver = getTimeOver(houseWork)

            binding.swipeView.translationX = 0f
            binding.success = houseWork.success
            binding.tvMainTitle.text = houseWork.houseWorkName
            binding.tvMainTime.text = getTime(houseWork)
            binding.tvMainArea.text = spaceNameMapper(houseWork.space)
            if(houseWork.feedbackHouseworkResponse!=null){
                binding.includeFeedback.houseworkFeedback0 = houseWork.feedbackHouseworkResponse["0"]
                binding.includeFeedback.houseworkFeedback1 = houseWork.feedbackHouseworkResponse["1"]
                binding.includeFeedback.houseworkFeedback2 = houseWork.feedbackHouseworkResponse["2"]
                binding.includeFeedback.houseworkFeedback3 = houseWork.feedbackHouseworkResponse["3"]
                binding.includeFeedback.houseworkFeedback4 = houseWork.feedbackHouseworkResponse["4"]
                binding.includeFeedback.houseworkFeedback5 = houseWork.feedbackHouseworkResponse["5"]
                binding.includeFeedback.houseworkFeedback6 = houseWork.feedbackHouseworkResponse["6"]
            }


            if (!houseWork.success) {
                binding.includeFeedback.root.visibility = View.GONE
            } else {
                binding.includeFeedback.root.visibility = View.VISIBLE
            }

            binding.flHouseWork.setOnClickListener {
                onClick.invoke(houseWork)
            }

            binding.flHouseWork.setOnLongClickListener {
                    onLongClick.invoke(
                        binding.root,
                        houseWork.success,
                        getTimeOver(houseWork),
                        isEmojiEmpty(houseWork.feedbackHouseworkResponse),
                houseWork)
                return@setOnLongClickListener true
            }
            val adapter = SmallProfileAdapter(houseWork.assignees.toMutableList())
            binding.rvProfileAdapter.adapter = adapter

            binding.includeFeedback.root.setOnClickListener {
                Timber.d("housework Id : ${houseWork.houseWorkCompleteId}")
                feedbackClick.invoke(houseWork.houseWorkCompleteId)
            }


        }
    }

    private fun getTimeOver(houseWork: HouseWork): Boolean {
        return when {
            houseWork.success -> false
            houseWork.scheduledTime == null -> remoteFormat.format(Calendar.getInstance().time) > houseWork.scheduledDate
            else ->
                if (remoteFormat.format(Calendar.getInstance().time) == houseWork.scheduledDate) {
                    format.format(Calendar.getInstance().time) > houseWork.scheduledTime
                } else remoteFormat.format(Calendar.getInstance().time) > houseWork.scheduledDate
        }
    }

    private fun isEmojiEmpty(feedbackHouseworkResponse: Map<String, FeedbackHouseworkResponse>?): Boolean {
        if(feedbackHouseworkResponse==null)return true
        val totalFeedbackNum = feedbackHouseworkResponse.values.sumOf { it.feedbackNum }
        return totalFeedbackNum == 0
    }
}