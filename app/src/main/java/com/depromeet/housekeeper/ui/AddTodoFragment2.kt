package com.depromeet.housekeeper.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.AddTodoChoreAdapter
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddTodo2Binding
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.Calendar

class AddTodoFragment2 : Fragment() {
    lateinit var binding: FragmentAddTodo2Binding
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addTodoChoreAdapter: AddTodoChoreAdapter
    private val addTodo2ViewModel: AddTodo2ViewModel by viewModels()
    private val navArgs by navArgs<AddTodoFragment2Args>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo2, container, false)
    binding.lifecycleOwner = this.viewLifecycleOwner
    binding.vm = addTodo2ViewModel
    binding.currentDate = "${navArgs.selectDate.date}요일"
    return binding.root
  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingVm()
        initListener()
        setAdapter()
    }

    private fun bindingVm() {
        val choreNames = navArgs.spaceChores.houseWorks
        val space = navArgs.spaceChores.spaceName
        addTodo2ViewModel.updateSpace(space)
        addTodo2ViewModel.initChores(addTodo2ViewModel.getSpace(), choreNames, navArgs.selectDate.date)
        Timber.d(addTodo2ViewModel.getChores().toString())

      addTodo2ViewModel.setDate(navArgs.selectDate.date)
      lifecycleScope.launchWhenStarted {
        addTodo2ViewModel.selectCalendar.collect {
          binding.addTodo2DateTv.text = "${it}요일"
        }
      }
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
            isEnabled = true

            setOnClickListener {
                // 마지막 position update
                updateChore(addTodo2ViewModel.getPosition(PositionType.CUR))

                // 집안일 생성 api
                addTodo2ViewModel.createHouseWorks()

                // 화면 전환
                it.findNavController().navigate(R.id.action_addTodoFragment2_to_mainFragment)
            }
        }

        binding.todoTimePicker.setOnTimeChangedListener { _, hour, _ ->
            binding.addTodo2AllDayCheckBox.isChecked = false
            val min = binding.todoTimePicker.getDisplayedMinutes() // 10분 단위로 받는 메소드
            addTodo2ViewModel.updateTime(hour, min)
        }

        binding.addTodo2DateTv.setOnClickListener {
          createDatePickerDialog()
        }
    }

  private fun createDatePickerDialog() {
    val selectDate = navArgs.selectDate.date

    val calendar = Calendar.getInstance().apply {
      set(Calendar.YEAR, selectDate.split("-")[0].toInt())
      set(Calendar.MONTH,selectDate.split("-")[1].toInt())
      set(Calendar.DAY_OF_MONTH,selectDate.split("-")[2].toInt())
    }

    val datePickerDialog = DatePickerDialog(
      this.requireContext(),
      { _, year, month, dayOfMonth ->
        addTodo2ViewModel.updateCalendarView(year, month, dayOfMonth)
      },
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH)-1,
      calendar.get(Calendar.DAY_OF_MONTH),
    )
    datePickerDialog.show()
  }


    private fun setAdapter() {
        // chore list rv adapter
        addTodoChoreAdapter = AddTodoChoreAdapter(addTodo2ViewModel.getChores())
        binding.addTodoChoreListRv.adapter = addTodoChoreAdapter

        addTodoChoreAdapter.setMyItemClickListener(object: AddTodoChoreAdapter.MyItemClickListener{
            override fun onItemClick(position: Int) {
                // 현재 chore 클릭하면 이전 chore 정보 업데이트
                addTodo2ViewModel.updatePositions(position)
                val prePos = addTodo2ViewModel.getPosition(PositionType.PRE)
                updateChore(prePos)

                // 현재 chore 기준으로 뷰 업데이트
                updateView(position)
            }
        })

        addTodoChoreAdapter.setMyItemRemoveListener(object: AddTodoChoreAdapter.MyRemoveClickListener{
            override fun onRemoveClick(position: Int) {
                // 현재 select 된 pos 정보 -> select 되지 않아도 remove 가능하기 때문
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
            binding.addTodo2AllDayCheckBox.isChecked ->  addTodo2ViewModel.updateChore(null, position)
            else -> addTodo2ViewModel.updateChore(addTodo2ViewModel.curTime.value?:"", position)
        }
        Timber.d(addTodo2ViewModel.chores.value.toString())
    }

    private fun updateView(position: Int) {
        val chore = addTodo2ViewModel.getChore(position)
        if(chore.scheduledTime == null) {
            binding.todoTimePicker.initDisPlayedValue()
            binding.addTodo2AllDayCheckBox.isChecked = true
        }
        else {
            val time = parseTime(chore.scheduledTime!!)
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