package com.depromeet.housekeeper

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

class AddTodoFragment2 : Fragment() {
    lateinit var binding: FragmentAddTodo2Binding
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addTodoChoreAdapter: AddTodoChoreAdapter
    private val addTodo2ViewModel: AddTodo2ViewModel by viewModels()
    private var chores: ArrayList<Chore> = arrayListOf(Chore(), Chore(), Chore(), Chore())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo2, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = addTodo2ViewModel

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
                updateChore(addTodo2ViewModel.getPosition(PositionType.CUR))

                // api

                // 화면 전환
                it.findNavController().navigate(R.id.action_addTodoFragment2_to_mainFragment)
            }
        }

        binding.todoTimePicker.setOnTimeChangedListener { _, hour, _ ->
            binding.addTodo2AllDayCheckBox.isChecked = false
            val min = binding.todoTimePicker.getDisplayedMinutes() // 10분 단위로 받는 메소드
            addTodo2ViewModel.updateTime(hour, min)
            Timber.d(addTodo2ViewModel.curTime.value)
        }
    }

    private fun setAdapter() {
        // chore list rv adapter
        val str = resources.getStringArray(R.array.chore_array)
        chores.mapIndexed { index, chore -> chore.houseWorkName = str[index] }

        addTodoChoreAdapter = AddTodoChoreAdapter(chores)
        binding.addTodoChoreListRv.adapter = addTodoChoreAdapter

        addTodoChoreAdapter.setMyItemClickListener(object: AddTodoChoreAdapter.MyItemClickListener{
            override fun onItemClick(position: Int) {
                // 현재 chore 클릭하면 이전 chore 정보 업데이트
                // positions.add(position)
                // val prePos = positions[positions.size - 2]
                addTodo2ViewModel.updatePositions(position)
                val prePos = addTodo2ViewModel.getPosition(PositionType.PRE)
                updateChore(prePos)

                // 현재 chore 기준으로 뷰 업데이트
                updateView(position)
            }
        })

        addTodoChoreAdapter.setMyItemRemoveListener(object: AddTodoChoreAdapter.MyRemoveClickListener{
            override fun onRemoveClick(position: Int) {
                // 현재 Select 된 Pos 정보 -> select 되지 않아도 remove 가능하기 때문
                val selectedPos = addTodoChoreAdapter.selectedChore.indexOf(1)

                if(addTodo2ViewModel.getPosition(PositionType.CUR) != selectedPos) {
                    addTodo2ViewModel.updatePositions(selectedPos)
                }
                updateView(addTodo2ViewModel.getPosition(PositionType.CUR))
            }

        })


        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addTodoRepeatRv.adapter = dayRepeatAdapter

    }

    private fun updateChore(position: Int) {
        when {
            binding.addTodo2AllDayCheckBox.isChecked ->  chores[position].scheduleTime = Chore.DEFAULT_TIME
            else -> chores[position].scheduleTime = addTodo2ViewModel.curTime.value
        }
    }

    private fun updateView(position: Int) {
        if(chores[position].scheduleTime == Chore.DEFAULT_TIME) {
            binding.todoTimePicker.initDisPlayedValue()
            binding.addTodo2AllDayCheckBox.isChecked = true
        }
        else {
            val time = parseTime(chores[position].scheduleTime)
            Timber.d(time.toString())
            binding.todoTimePicker.setDisPlayedValue(time.first, time.second)
        }
    }

    private fun parseTime(time: String): Pair<Int, Int>{
        val temp = time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        return Pair(hour, min)
    }

}