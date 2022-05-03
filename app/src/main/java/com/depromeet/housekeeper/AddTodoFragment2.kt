package com.depromeet.housekeeper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.depromeet.housekeeper.adapter.AddTodoChoreAdapter
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddTodo2Binding
import com.depromeet.housekeeper.model.Chore
import timber.log.Timber
import java.text.SimpleDateFormat

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
                // 마지막 position update

                // api

                // 화면 전환
                it.findNavController().navigate(R.id.action_addTodoFragment2_to_mainFragment)
            }
        }

        binding.todoTimePicker.setOnTimeChangedListener { _, hour, _ ->
            binding.addTodo2AllDayCheckBox.isChecked = false
            val min = binding.todoTimePicker.getDisplayedMinutes() // 10분 단위로 받는 메소드
            curTime = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
        }

    }

    private fun setAdapter() {
        // chore list rv adapter
        val defaultTime = resources.getString(R.string.add_todo_default_time)
        val str = resources.getStringArray(R.array.chore_array)
        val chores: ArrayList<Chore> = arrayListOf(Chore(), Chore(), Chore(), Chore())
        chores.mapIndexed { index, chore -> chore.houseWorkName = str[index] }

        addTodoChoreAdapter = AddTodoChoreAdapter(chores)
        binding.addTodoChoreListRv.adapter = addTodoChoreAdapter

        val positions: ArrayList<Int> = arrayListOf(0)

        addTodoChoreAdapter.setMyItemClickListener(object: AddTodoChoreAdapter.MyItemClickListener{
            override fun onItemClick(position: Int) {
                // 현재 chore 클릭하면 이전 chore 정보 업데이트
                Timber.d(chores.toString())
                positions.add(position)
                val prePos = positions[positions.size - 2]
                if(binding.addTodo2AllDayCheckBox.isChecked) {
                    chores[prePos].scheduleTime = defaultTime
                }
                else {
                    chores[prePos].scheduleTime = curTime
                }

                // 현재 chore 기준으로 뷰 업데이트
                if(chores[position].scheduleTime == defaultTime) {
                    binding.todoTimePicker.initDisPlayedValue()
                    binding.addTodo2AllDayCheckBox.isChecked = true
                }
                else {
                    val time = parseTime(chores[position].scheduleTime)
                    Timber.d(time.toString())
                    binding.todoTimePicker.setDisPlayedValue(time.first, time.second / 10)
                }


            }
        })

        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addTodoRepeatRv.adapter = dayRepeatAdapter

    }

    private fun parseTime(time: String): Pair<Int, Int>{
        val temp = time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        return Pair(hour, min)
    }

}