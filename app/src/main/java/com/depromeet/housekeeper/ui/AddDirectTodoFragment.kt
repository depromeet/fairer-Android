package com.depromeet.housekeeper.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddDirectTodoBinding
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.enums.ViewType
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.Calendar

class AddDirectTodoFragment : Fragment() {
    lateinit var binding: FragmentAddDirectTodoBinding
    lateinit var imm: InputMethodManager
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    private val viewModel: AddDirectTodoViewModel by viewModels()
    private val navArgs by navArgs<AddDirectTodoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_direct_todo, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewModel
        binding.currentDate = "${navArgs.selectDate.date}요일"


        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        bindingVm()
        initListener()
    }

    private fun bindingVm() {
        viewModel.setViewType(navArgs.viewType)

        when(viewModel.curViewType.value) {
            ViewType.ADD -> {
                viewModel.initDirectChore()
            }
            ViewType.EDIT -> {
                onEditView()
            }
        }

        lifecycleScope.launchWhenStarted {
          viewModel.selectCalendar.collect {
            binding.addDirectTodoDateTv.text = "${it}요일"
          }
        }
    }

    private fun initListener() {
        binding.addDirectTodoBackgroundCl.setOnClickListener {
            hideKeyboard(binding.addDirectTodoTitleEt)
        }

        binding.addDirectTodoTitleEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.addDirectTodoDoneBtn.mainFooterButton.isEnabled = binding.addDirectTodoTitleEt.text.isNotEmpty()
            }

        })

        binding.addDirectTodoTimePicker.setOnTimeChangedListener { _, hour, _ ->
            binding.addDirectTodoAllDayCheckBox.isChecked = false
            val min = binding.addDirectTodoTimePicker.getDisplayedMinutes() // 10분 단위로 받는 메소드
            viewModel.updateTime(hour, min)
        }

        binding.addDirectTodoHeader.apply {
            addTodoBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
            addTodoHeaderTv.text = ""

            // delete 분기 처리
            when(viewModel.curViewType.value) {
                ViewType.ADD -> {
                    addTodoHeaderRightTv.visibility = View.GONE
                }
                ViewType.EDIT -> {
                    addTodoHeaderRightTv.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            // delete api
                            viewModel.deleteHouseWork()
                            it.findNavController().navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
                        }
                    }
                }
            }
        }

        binding.addDirectTodoDoneBtn.mainFooterButton.apply {
            // edit 분기 처리
            when(viewModel.curViewType.value) {
                ViewType.ADD -> {
                    text = resources.getString(R.string.add_todo_done_btn_txt)
                    setOnClickListener {
                        // 집안일 이름 & 시간 업데이트
                        updateChore()
                        // 집안일 생성 api
                        viewModel.createHouseWorks()
                        it.findNavController().navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
                    }
                }

                ViewType.EDIT -> {
                    text = resources.getString(R.string.edit_todo_btn_tv)
                    // 집안일 수정 api
                    setOnClickListener {

                    }
                }
            }
        }

        binding.addDirectTodoDateTv.setOnClickListener {
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
                viewModel.updateCalendarView(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH)-1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()
    }

    private fun onEditView() {
        // set arg
        val houseWork = navArgs.houseWork
        val assignees: List<Int> = arrayListOf()
        houseWork.assignees.map {
            assignees.plus(it.memberId)
        }
        val chore = Chore(assignees, houseWork.houseWorkName, houseWork.scheduledDate, houseWork.scheduledTime, houseWork.space)

        // viewmodel update
        viewModel.initEditChore(chore)

        // ui update
    }

    private fun updateChore() {
        // name set
        viewModel.updateChoreName(binding.addDirectTodoTitleEt.text.toString())

        // time set
        when {
            binding.addDirectTodoAllDayCheckBox.isChecked ->  viewModel.updateChoreTime(null)
            else -> viewModel.updateChoreTime(viewModel.curTime.value!!)
        }
        Timber.d(viewModel.chores.value.toString())
    }

    private fun setAdapter() {
        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addDirectTodoRepeatRv.adapter = dayRepeatAdapter
    }

    private fun hideKeyboard(v: View) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }


}