package com.depromeet.housekeeper.ui.addHousework.selectTime

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentAddHouseWorkBinding
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddAssigneeAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddHouseWorkChoreAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.ui.custom.dialog.AssigneeBottomSheetDialog
import com.depromeet.housekeeper.ui.custom.timepicker.FairerTimePicker
import com.depromeet.housekeeper.util.dp2px
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AddHouseWorkFragment :
    BaseFragment<FragmentAddHouseWorkBinding>(R.layout.fragment_add_house_work) {
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addHouseWorkChoreAdapter: AddHouseWorkChoreAdapter
    lateinit var addAssigneeAdapter: AddAssigneeAdapter
    private val viewModel: AddHouseWorkViewModel by viewModels()
    private val navArgs by navArgs<AddHouseWorkFragmentArgs>()

    override fun createView(binding: FragmentAddHouseWorkBinding) {
        binding.vm = viewModel
        viewModel.addCalendarView(navArgs.selectDate.date)
    }

    override fun viewCreated() {
        setAdapter()
        bindingVm()
        initListener()
        initView()
    }

    private fun initView() {
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
        binding.currentDate = viewModel.bindingDate()
        binding.doRepeatMonthly = false
        setRepeatTextView()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectCalendar.collect {
                binding.addHouseWorkDateTv.text = viewModel.bindingDate()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.curAssignees.collect {
                addAssigneeAdapter.updateAssignees(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.createdSucess.collect{
                if (it) findNavController().popBackStack(R.id.SelectSpaceFragment, true)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
                if (it) {
                    fragmentManager?.popBackStack(
                        R.id.SelectSpaceFragment,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        binding.switchHouseworkTime.isChecked = false
        binding.switchHouseworkRepeat.isChecked = false
        // header title
        binding.addHouseWorkHeader.defaultHeaderTitleTv.text = ""

        // header back button
        binding.addHouseWorkHeader.defaultHeaderBackBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.addHouseWorkDoneBtn.mainFooterButton.apply {
            text = resources.getString(R.string.add_todo_done_btn_txt)
            isEnabled = true

            setOnClickListener {
                // 마지막 position update
                updateChore(viewModel.getPosition(PositionType.CUR))
                viewModel.updateChoreDate()

                // 집안일 생성 api
                viewModel.createHouseWorks()
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
            setOnCheckedChangeListener { _, isChecked ->
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

        binding.addHouseWorkDateTv.setOnClickListener {
            createDatePickerDialog()
        }

        binding.addAssigneeLayout.setOnClickListener {
            createBottomSheet()
        }

        binding.clRepeatCycle.setOnClickListener {
            binding.spinnerRepeat.performClick()
        }

        binding.btnSpinnerDropdown.setOnClickListener {
            binding.spinnerRepeat.performClick()
        }

        binding.spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, p3: Long) {
                binding.doRepeatMonthly = pos == 1
                setRepeatTextView()
                if (pos == 1) {
                    viewModel.updateRepeatInform(RepeatCycle.MONTHLY)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    private fun setAdapter() {
        // 집안일 담당자 adapter
        addAssigneeAdapter = AddAssigneeAdapter(viewModel.curAssignees.value)
        binding.addAssigneeRv.adapter = addAssigneeAdapter

        // 집안일 adapter
        val choreNames = navArgs.spaceChores.houseWorks
        val space = navArgs.spaceChores.spaceName
        viewModel.updateSpace(space)
        viewModel.setDate(navArgs.selectDate.date)

        viewModel.initChores(viewModel.getSpace(), choreNames)
        addHouseWorkChoreAdapter = AddHouseWorkChoreAdapter(viewModel.getChores())
        binding.addHouseWorkChoreListRv.adapter = addHouseWorkChoreAdapter

        addHouseWorkChoreAdapter.setMyItemClickListener(object :
            AddHouseWorkChoreAdapter.MyItemClickListener {
            override fun onItemClick(position: Int) {
                // 현재 chore 클릭하면 이전 chore 정보 업데이트
                viewModel.updatePositions(position)
                val prePos = viewModel.getPosition(PositionType.PRE)
                updateChore(prePos)

                // 현재 chore 기준으로 뷰 업데이트
                updateView(position)
            }
        })

        addHouseWorkChoreAdapter.setMyItemRemoveListener(object :
            AddHouseWorkChoreAdapter.MyRemoveClickListener {
            override fun onRemoveClick(position: Int) {
                // 현재 select 된 pos 정보 -> select 되지 않아도 remove 가능하기 때문
                val selectedPos = addHouseWorkChoreAdapter.selectedChore.indexOf(1)

                if (viewModel.getPosition(PositionType.CUR) != selectedPos) {
                    viewModel.updatePositions(selectedPos)
                }
                updateView(viewModel.getPosition(PositionType.CUR))
            }
        })


        setRepeatAdapter()

    }

    private fun setRepeatAdapter() {
        // 반복주기
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.repeat_cycle_array,
            R.layout.item_spinner
        ).also {
            it.setDropDownViewResource(R.layout.item_spinner_dropdown)
            binding.spinnerRepeat.dropDownVerticalOffset = dp2px(requireContext(), 30f).toInt()
            binding.spinnerRepeat.dropDownHorizontalOffset = -dp2px(requireContext(), 26f).toInt()
            binding.spinnerRepeat.adapter = it
        }

        // 요일 반복 adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.rvAddHouseWorkRepeat.layoutManager = GridLayoutManager(context, 7)
        binding.rvAddHouseWorkRepeat.adapter = dayRepeatAdapter
        dayRepeatAdapter.setDayItemClickListener(object :
            DayRepeatAdapter.DayItemClickListener {
            override fun onItemClick(selectedDays: Array<Boolean>) {
                val repeatDays = viewModel.getRepeatDays(selectedDays)
                binding.repeatDaySelected = repeatDays.isNotEmpty()

                var repeatDaysString = viewModel.getRepeatDaysString("eng")
                viewModel.updateRepeatInform(repeatDaysString)

                repeatDaysString = viewModel.getRepeatDaysString("kor")
                binding.repeatDay = " ${repeatDaysString.joinToString(",")}요일"
            }
        })
    }

    private fun setRepeatTextView() {
        binding.repeatDay = " " + viewModel.getCurDay("일")

        if (binding.doRepeatMonthly == true) {
            binding.repeatCycle = getString(R.string.add_house_repeat_monthly)
        } else {
            binding.repeatCycle = getString(R.string.add_house_repeat_weekly)
            binding.repeatDay = " ${viewModel.getRepeatDaysString("kor").joinToString(",")}요일"
        }
    }

    private fun updateTime() {
        val time = binding.todoTimePicker.getDisPlayedTime()
        viewModel.updateTime(time.first, time.second)
    }

    private fun updateChore(position: Int) {
        when {
            binding.switchHouseworkTime.isChecked ->
                viewModel.updateChoreTime(viewModel.curTime.value, position)
            else -> viewModel.updateChoreTime(null, position)
        }
    }

    private fun updateView(position: Int) {
        val chore = viewModel.getChore(position)

        if (chore.scheduledTime == null) {
            binding.todoTimePicker.initDisPlayedValue()
            binding.switchHouseworkTime.isChecked = false
        } else {
            val time = parseTime(chore.scheduledTime!!)
            binding.todoTimePicker.setDisPlayedValue(time.first, time.second)
            binding.switchHouseworkTime.isChecked = true
        }

        when (chore.repeatCycle) {
            RepeatCycle.ONCE.value -> {
                binding.isRepeatChecked = false
                binding.doRepeatMonthly = false
                binding.repeatDaySelected = false
                binding.spinnerRepeat.setSelection(0)
                dayRepeatAdapter.updateSelectedDays(RepeatCycle.DAYILY, false)
            }
            RepeatCycle.WEEKLY.value -> {
                binding.isRepeatChecked = true
                binding.doRepeatMonthly = false
                binding.repeatDaySelected = true
                binding.spinnerRepeat.setSelection(0)
                dayRepeatAdapter.updateSelectedDays(chore.repeatPattern)
            }
            RepeatCycle.MONTHLY.value -> {
                binding.isRepeatChecked = true
                binding.doRepeatMonthly = true
                binding.spinnerRepeat.setSelection(1)
            }
        }
        setRepeatTextView()

        val curAssignees = arrayListOf<Assignee>()
        viewModel.allGroupInfo.value.map { assignee ->
            chore.assignees.map { curId ->
                if (assignee.memberId == curId) {
                    curAssignees.add(assignee)
                }
            }
        }
        viewModel.setCurAssignees(curAssignees)
        addAssigneeAdapter.updateAssignees(viewModel.getCurAssignees())
    }

    private fun createBottomSheet() {
        val bottomSheet = AssigneeBottomSheetDialog(
            allGroup = viewModel.allGroupInfo.value,
            curAssignees = viewModel.curAssignees.value
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
        bottomSheet.setMyOkBtnClickListener(object :
            AssigneeBottomSheetDialog.MyOkBtnClickListener {

            // 담당자 1명 이상 select 될때만 작동
            override fun onOkBtnClick() {
                viewModel.setCurAssignees(bottomSheet.selectedAssignees)
                addAssigneeAdapter.updateAssignees(viewModel.getCurAssignees())
                viewModel.updateAssigneeId(position = viewModel.getPosition(PositionType.CUR))
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
                if (binding.doRepeatMonthly == true) binding.repeatDay = " ${dayOfMonth}일"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) - 1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val temp = time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        return Pair(hour, min)
    }


}