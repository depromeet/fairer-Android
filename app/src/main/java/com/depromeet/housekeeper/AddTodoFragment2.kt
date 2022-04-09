package com.depromeet.housekeeper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.FragmentAddTodo2Binding
import com.depromeet.housekeeper.databinding.ItemTodoRepeatDayBtnBinding

class AddTodoFragment2 : Fragment() {
    lateinit var binding: FragmentAddTodo2Binding
    lateinit var dayRepeatRVAdapter: DayRepeatRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo2, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        initListener()
        initRepeatBtn()

        return binding.root
    }

    private fun initListener() {
        binding.addTodo2Header.addTodoHeaderTv.text = "집안일 선택"

        binding.addTodo2Header.addTodoBackBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    private fun initRepeatBtn() {
        binding.addTodoRepeatRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dayRepeatRVAdapter = DayRepeatRVAdapter()
        binding.addTodoRepeatRv.adapter = dayRepeatRVAdapter
    }

    inner class DayRepeatRVAdapter: RecyclerView.Adapter<DayRepeatRVAdapter.ViewHolder>(){
        private val days: Array<String> = resources.getStringArray(R.array.day_array)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DayRepeatRVAdapter.ViewHolder {
            val binding: ItemTodoRepeatDayBtnBinding = ItemTodoRepeatDayBtnBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DayRepeatRVAdapter.ViewHolder, position: Int) {
            holder.bind(days[position])
        }

        override fun getItemCount(): Int = days.size

        inner class ViewHolder(private val binding: ItemTodoRepeatDayBtnBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(day: String) {
                binding.itemDayRepeatBtn.text = day

                binding.itemDayRepeatBtn.setOnClickListener {
                    binding.itemDayRepeatBtn.isSelected = !binding.itemDayRepeatBtn.isSelected
                }
            }
        }
    }
}