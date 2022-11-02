package com.depromeet.housekeeper.ui.addDirectTodo

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentAddDirectTodoBinding
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.request.EditChore
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddAssigneeAdapter
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.ui.custom.dialog.AssigneeBottomSheetDialog
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import com.depromeet.housekeeper.ui.custom.timepicker.FairerTimePicker
import com.depromeet.housekeeper.util.dp2px
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
            ViewType.ADD -> { viewModel.initDirectChore() }
            ViewType.EDIT -> { onEditView() }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.curAssignees.collect {
                addAssigneeAdapter.updateAssignees(it)
            }
        }

        val space = if (viewModel.editChore.value != null) {
            viewModel.editChore.value!!.space
        } else {
            viewModel.chores.value[0].space
        }
        binding.space = spaceNameMapper(space)

        lifecycleScope.launchWhenStarted {
            viewModel.selectCalendar.collect {
                binding.addDirectTodoDateTv.text = viewModel.bindingDate()
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

        initEditTextListener()

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
                binding.isTimeChecked = isChecked
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
                it.findNavController().popBackStack()
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

        binding.clAddDirectTodoRepeatCycle.setOnClickListener {
            binding.spinnerRepeat.performClick()
        }

        binding.btnSpinnerDropdown.setOnClickListener {
            binding.spinnerRepeat.performClick()
        }

        binding.spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, p3: Long) {
                binding.doRepeatMontly = pos == 1
                setRepeatTextView()
                if (pos == 1) {
                    viewModel.updateRepeatInform(RepeatCycle.MONTHLY)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setRepeatTextView() {
        binding.repeatDay = " " + viewModel.getCurDay("일")

        if (binding.doRepeatMontly == true) {
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


    private fun onEditView() {
        val houseWork: HouseWork = navArgs.houseWork
        Timber.d("TAG $houseWork")
        viewModel.initEditChore(houseWork)
        viewModel.setHouseWorkId(houseWork.houseWorkId)
        if (houseWork.repeatCycle== RepeatCycle.WEEKLY.value) {
            viewModel.setSelectedDayList(houseWork.repeatPattern!!)
        }

        initEditView()
    }

    private fun initEditView() {
        val editChore = viewModel.editChore.value!!

        binding.addDirectTodoTitleEt.fairerEt.setText(editChore.houseWorkName)

        Timber.d("시간 : ${editChore.scheduledTime}")
        if (editChore.scheduledTime != null) {
            binding.isTimeChecked = true
            Timber.d("시간 null 아님: ${editChore.scheduledTime}, ${binding.isTimeChecked}")
            val time: Pair<Int, Int> = parseTime(editChore.scheduledTime!!)
            binding.todoTimePicker.setDisPlayedValue(time.first, time.second)
        }

        initEditRepeatView(editChore)
    }

    private fun initEditRepeatView(editChore: EditChore){
        when (editChore.repeatCycle){
            RepeatCycle.MONTHLY.value ->{
                binding.isRepeatChecked = true
                binding.spinnerRepeat.setSelection(1)
                binding.doRepeatMontly = true
                binding.repeatCycle = getString(R.string.add_house_repeat_monthly)
                binding.repeatDay = viewModel.getCurDay("일")
            }
            RepeatCycle.WEEKLY.value -> {
                binding.isRepeatChecked = true
                binding.doRepeatMontly = false
                binding.repeatDaySelected = true
                dayRepeatAdapter.updateSelectedDays(viewModel.selectedDayList)
                dayRepeatAdapter.notifyDataSetChanged()
                binding.repeatCycle = getString(R.string.add_house_repeat_weekly)
                binding.repeatDay = " ${viewModel.getRepeatDaysString("kor").joinToString(",")}요일"
            }
            RepeatCycle.DAYILY.value -> {
                binding.isRepeatChecked = true
                binding.doRepeatMontly = false
                binding.repeatDaySelected = true
                dayRepeatAdapter.updateSelectedDays(RepeatCycle.DAYILY)
                dayRepeatAdapter.notifyDataSetChanged()
                binding.repeatCycle = getString(R.string.add_house_repeat_weekly)
                binding.repeatDay = " ${viewModel.getRepeatDaysString("kor").joinToString(",")}요일"
            }
            else -> {
                binding.isRepeatChecked = false
            }
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

        setRepeatAdapter()
    }

    private fun setRepeatAdapter(){
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

        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.rvAddDirectTodoRepeat.layoutManager = GridLayoutManager(context, 7)
        binding.rvAddDirectTodoRepeat.adapter = dayRepeatAdapter
        dayRepeatAdapter.setDayItemClickListener(object :
            DayRepeatAdapter.DayItemClickListener{
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

    private fun initEditTextListener(){
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


    /**
     * Dialog
     */

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