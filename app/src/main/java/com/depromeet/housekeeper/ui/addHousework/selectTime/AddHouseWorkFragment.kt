package com.depromeet.housekeeper.ui.addHousework.selectTime

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentAddHouseWorkBinding
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddAssigneeAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddHouseWorkChoreAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.ui.custom.dialog.AssigneeBottomSheetDialog
import com.depromeet.housekeeper.ui.custom.timepicker.FairerTimePicker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AddHouseWorkFragment : BaseFragment<FragmentAddHouseWorkBinding>(R.layout.fragment_add_house_work) {
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    lateinit var addHouseWorkChoreAdapter: AddHouseWorkChoreAdapter
    lateinit var addAssigneeAdapter: AddAssigneeAdapter
    private val viewModel: AddHouseWorkViewModel by viewModels()
    private val navArgs by navArgs<AddHouseWorkFragmentArgs>()

    override fun createView(binding: FragmentAddHouseWorkBinding) {
        binding.vm = viewModel
        viewModel.addCalendarView(navArgs.selectDate.date)
        binding.currentDate = viewModel.bindingDate()
    }

    override fun viewCreated() {
        setAdapter()
        bindingVm()
        initListener()
        initView()
    }

    private fun initView(){
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
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
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
                if (it) {
                    fragmentManager?.popBackStack(R.id.SelectSpaceFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

        binding.addHouseWorkDateTv.setOnClickListener {
            createDatePickerDialog()
        }

        binding.addAssigneeLayout.setOnClickListener {
            createBottomSheet()
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

        // 요일 반복 adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addHouseWorkRepeatRv.adapter = dayRepeatAdapter
    }

    private fun updateTime() {
        val time = binding.todoTimePicker.getDisPlayedTime()
        viewModel.updateTime(time.first, time.second)
    }

    private fun updateChore(position: Int) {
        when {
            binding.switchHouseworkTime.isChecked -> viewModel.updateChore(viewModel.curTime.value, position)
            else -> viewModel.updateChore(null, position)
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