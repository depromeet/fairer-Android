package com.depromeet.housekeeper.ui.addDirectTodo

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentAddDirectTodoBinding
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddAssigneeAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.ui.custom.dialog.AssigneeBottomSheetDialog
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import com.depromeet.housekeeper.ui.custom.timepicker.FairerTimePicker
import com.depromeet.housekeeper.util.spaceNameMapper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AddDirectTodoFragment : BaseFragment<FragmentAddDirectTodoBinding>(R.layout.fragment_add_direct_todo) {
    lateinit var imm: InputMethodManager
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addAssigneeAdapter: AddAssigneeAdapter
    private val viewModel: AddDirectTodoViewModel by viewModels()
    private val navArgs by navArgs<AddDirectTodoFragmentArgs>()

    override fun createView(binding: FragmentAddDirectTodoBinding) {
        binding.vm = viewModel
        viewModel.addCalendarView(navArgs.selectDate.date)
        binding.currentDate = viewModel.bindingDate()

        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun viewCreated() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        setAdapter()
        initView()
        bindingVm()
        initListener()
    }

    private fun initView(){
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    private fun bindingVm() {
        viewModel.setViewType(navArgs.viewType)
        viewModel.setCurrentDate(navArgs.selectDate.date)

        when (viewModel.curViewType.value) {
            ViewType.ADD -> {
                viewModel.initDirectChore()
            }
            ViewType.EDIT -> {
                onEditView()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.curAssignees.collect {
                addAssigneeAdapter.updateAssignees(it)
            }
        }

        binding.space = spaceNameMapper(viewModel.chores.value[0].space)

        lifecycleScope.launchWhenStarted {
            viewModel.selectCalendar.collect {
                binding.addDirectTodoDateTv.text = viewModel.bindingDate()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
            }
        }

    }

    private fun initListener() {
        binding.switchHouseworkTime.isChecked = false
        binding.switchHouseworkRepeat.isChecked = false
        binding.addDirectTodoTitleEt.signNameClear.setOnClickListener {
            binding.addDirectTodoTitleEt.fairerEt.setText(R.string.sign_name_blank)
        }
        binding.addDirectTodoBackgroundCl.setOnClickListener {
            hideKeyboard(binding.switchHouseworkTime)
            binding.addDirectTodoTitleEt.fairerEt.isEnabled = false
            binding.isTextChanged = false
            binding.addDirectTodoTitleEt.fairerEt.isEnabled = true
        }
        binding.addDirectTodoTitleEt.fairerEt.hint = getString(R.string.add_direct_todo_title_hint)

        val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"
        binding.addDirectTodoTitleEt.fairerEt.addTextChangedListener {
            val value: String = binding.addDirectTodoTitleEt.fairerEt.text.toString()
            binding.isTextChanged = true
            if (!value.matches(pattern.toRegex())) {
                binding.isError = true
                binding.addDirectTodoDoneBtn.mainFooterButton.isEnabled = false
                binding.tvError.setText(R.string.sign_name_error)
            } else if(value.length>16) {
                binding.isError = true
                binding.addDirectTodoDoneBtn.mainFooterButton.isEnabled = false
                binding.tvError.setText(R.string.sign_name_text_over_error)
            } else {
                binding.isError = false
                binding.addDirectTodoDoneBtn.mainFooterButton.isEnabled =
                    value.isNotEmpty()
            }
            if (value == "") {
                binding.isTextChanged = false
            }
        }


        binding.todoTimePicker.setOnTimeChangedListener { _, _, _ ->
            updateTime()
        }

        binding.todoTimePicker.setMyMinChangedListener(object :
            FairerTimePicker.MyMinChangedListener {
            override fun onMinChange() {
                updateTime()
            }
        })

        binding.switchHouseworkTime.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                val time = binding.todoTimePicker.getDisPlayedTime()
                viewModel.updateTime(time.first, time.second)
                binding.timeSwitch = isChecked
            }
        }

        binding.switchHouseworkRepeat.apply {
            setOnCheckedChangeListener { compoundButton, isChecked ->
                binding.isRepeatChecked = isChecked
            }
        }

        binding.addAssigneeLayout.setOnClickListener {
            createBottomSheet()
        }

        binding.addDirectTodoHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
            defaultHeaderTitleTv.text = ""

            // delete 분기 처리
            when (viewModel.curViewType.value) {
                ViewType.ADD -> {
                    defaultHeaderRightTv.visibility = View.GONE
                }
                ViewType.EDIT -> {
                    defaultHeaderRightTv.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            // delete api
                            showDeleteDialog()
                        }
                    }
                }
            }
        }

        binding.addDirectTodoDoneBtn.mainFooterButton.apply {
            isEnabled = binding.addDirectTodoTitleEt.fairerEt.text.isNotEmpty()
            // edit 분기 처리
            when (viewModel.curViewType.value) {
                ViewType.ADD -> {
                    text = resources.getString(R.string.add_todo_done_btn_txt)
                    // 집안일 생성 api
                    setOnClickListener {
                        updateChore()
                        viewModel.createHouseWorks()
                        it.findNavController()
                            .navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
                    }
                }

                ViewType.EDIT -> {
                    text = resources.getString(R.string.edit_todo_btn_tv)
                    // 집안일 수정 api
                    setOnClickListener {
                        updateChore()
                        viewModel.editHouseWork()
                        it.findNavController()
                            .navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
                    }
                }
            }
        }

        binding.addDirectTodoDateTv.setOnClickListener {
            createDatePickerDialog()
        }
    }

    private fun updateTime() {
        val time = binding.todoTimePicker.getDisPlayedTime()
        viewModel.updateTime(time.first, time.second)
    }

    private fun createBottomSheet() {
        val bottomSheet = AssigneeBottomSheetDialog(
            allGroup = viewModel.allGroupInfo.value,
            curAssignees = viewModel.curAssignees.value
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
        bottomSheet.setMyOkBtnClickListener(object :
            AssigneeBottomSheetDialog.MyOkBtnClickListener {
            override fun onOkBtnClick() {
                viewModel.setCurAssignees(bottomSheet.selectedAssignees)
                addAssigneeAdapter.updateAssignees(viewModel.getCurAssignees())
                viewModel.updateAssigneeId()
                Timber.d(viewModel.chores.value.toString())
            }
        })
    }


    private fun createDatePickerDialog() {
        val selectDate = navArgs.selectDate.date

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectDate.split("-")[0].toInt())
            set(Calendar.MONTH, selectDate.split("-")[1].toInt())
            set(Calendar.DAY_OF_MONTH, selectDate.split("-")[2].toInt())
        }

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->
                viewModel.updateCalendarView(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) - 1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()
    }

    private fun onEditView() {
        // set arg
        val houseWork = navArgs.houseWork
        Timber.d("TAG $houseWork")
        val assignees: List<Int> = arrayListOf()
        houseWork.assignees.map {
            assignees.plus(it.memberId)
        }
        val chore = Chore(
            assignees = assignees,
            houseWorkName = houseWork.houseWorkName,
            scheduledDate = houseWork.scheduledDate,
            scheduledTime = houseWork.scheduledTime,
            space = houseWork.space
        )

        // viewmodel update
        viewModel.initEditChore(chore, houseWork.assignees)
        viewModel.setHouseWorkId(houseWork.houseWorkId)

        // ui update
        initUi()
    }

    private fun initUi() {
        binding.addDirectTodoTitleEt.fairerEt.setText(viewModel.chores.value[0].houseWorkName)
        if (viewModel.chores.value[0].scheduledTime != null) {
            val time: Pair<Int, Int> = parseTime(viewModel.chores.value[0].scheduledTime!!)
            binding.todoTimePicker.setDisPlayedValue(time.first, time.second)

        }
    }

    private fun updateChore() {
        // name set
        viewModel.updateChoreName(binding.addDirectTodoTitleEt.fairerEt.text.toString())

        // time set
        when {
            binding.switchHouseworkTime.isChecked -> viewModel.updateChoreTime(viewModel.curTime.value!!)
            else -> viewModel.updateChoreTime(null)
        }

        //date set
        viewModel.updateChoreDate()

        Timber.d(viewModel.chores.value.toString())
    }

    private fun setAdapter() {
        // 집안일 담당자 adapter
        addAssigneeAdapter = AddAssigneeAdapter(viewModel.curAssignees.value)
        binding.addAssigneeRv.adapter = addAssigneeAdapter

        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        //binding.addDirectTodoRepeatRv.adapter = dayRepeatAdapter
    }

    private fun hideKeyboard(v: View) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val temp = time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        return Pair(hour, min)
    }

    private fun showDeleteDialog() {
        val dialog = FairerDialog(requireContext(), DialogType.DELETE)
        dialog.showDialog()

        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                viewModel.deleteHouseWork()
                findNavController().navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
            }
        }
    }

}