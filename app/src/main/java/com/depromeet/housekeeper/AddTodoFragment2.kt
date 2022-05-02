package com.depromeet.housekeeper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.adapter.AddTodoChoreAdapter
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddTodo2Binding
import com.depromeet.housekeeper.databinding.ItemTodoRepeatDayBtnBinding
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.ui.custom.timepicker.FairerTimePicker
import kotlinx.serialization.StringFormat
import timber.log.Timber
import java.text.DateFormat

class AddTodoFragment2 : Fragment() {
    lateinit var binding: FragmentAddTodo2Binding
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addTodoChoreAdapter: AddTodoChoreAdapter
    private val addTodo2ViewModel: AddTodo2ViewModel by viewModels()
    private var curTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo2, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        initListener()
        setAdapter()

        return binding.root
    }

    private fun initListener() {
        // header title
        binding.addTodo2Header.addTodoHeaderTv.text = ""

        // header back button
        binding.addTodo2Header.addTodoBackBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.addTodo2DoneBtn.mainFooterButton.apply {
            text = resources.getString(R.string.add_todo_done_btn_txt)

            setOnClickListener {
                it.findNavController().navigate(R.id.action_addTodoFragment2_to_mainFragment)
            }
        }

        binding.todoTimePicker.setOnTimeChangedListener { _, hour, _ ->
            binding.addTodo2AllDayCheckBox.isChecked = false
            val min = binding.todoTimePicker.getDisplayedMinutes() // 10분 단위로 받는 메소드
            curTime = if(hour <= 12) {
                "오전\n${String.format("%02d", hour)}:${min}"
            } else {
                "오후\n${String.format("%02d", hour - 12)}:${min}"
            }
        }

    }

    private fun setAdapter() {
        // chore list rv adapter
        val defaultTime = resources.getString(R.string.add_todo_default_time)
        val str = resources.getStringArray(R.array.chore_array)
        val chores: ArrayList<Chore> = arrayListOf(Chore(), Chore(), Chore(), Chore())
        chores.mapIndexed { index, chore -> chore.houseWorkName = str[index] }
        Timber.d(chores.toString())

        addTodoChoreAdapter = AddTodoChoreAdapter(chores)
        binding.addTodoChoreListRv.adapter = addTodoChoreAdapter

        var positions: ArrayList<Int> = arrayListOf(0)

        addTodoChoreAdapter.setMyItemClickListener(object: AddTodoChoreAdapter.MyItemClickListener{
            override fun onItemClick(curPos: Int) {
                // 현재 클릭하면 이전 chore 정보 업데이트 - 기준 현재 time picker & check box
                positions.add(curPos)
                val prePos = positions[positions.size - 2]
                if(binding.addTodo2AllDayCheckBox.isChecked) {
                    chores[prePos].scheduleTime = defaultTime
                }
                else {
                    chores[prePos].scheduleTime = curTime
                }

                // 현재 chore 기준으로 뷰 업데이트
                if(chores[curPos].scheduleTime == defaultTime) {
                    binding.addTodo2AllDayCheckBox.isChecked = true
                }


            }

            override fun onRemoveChore(position: Int) {
//                chores.removeAt(position)
                Timber.d("남은 집안일 : ${chores.toString()}")
            }
        })

        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addTodoRepeatRv.adapter = dayRepeatAdapter

    }

    private fun getTime() {

    }

}