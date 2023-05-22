package com.depromeet.housekeeper.ui.addHousework.selectSpace

import android.app.DatePickerDialog
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentSelectSpaceBinding
import com.depromeet.housekeeper.model.FeedbackHouseworkResponse
import com.depromeet.housekeeper.model.SpaceChores
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.ui.addHousework.selectSpace.adapter.SelectSpaceChoreAdapter
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import com.depromeet.housekeeper.util.VerticalItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class SelectSpaceFragment :
    BaseFragment<FragmentSelectSpaceBinding>(R.layout.fragment_select_space), View.OnClickListener {
    private lateinit var myAdapter: SelectSpaceChoreAdapter
    private val viewModel: SelectSpaceViewModel by viewModels()
    private val navArgs by navArgs<SelectSpaceFragmentArgs>()

    override fun createView(binding: FragmentSelectSpaceBinding) {
        viewModel.addCalendarView(navArgs.selectDate.date)
        Timber.d("TAG ${navArgs.selectDate.date}")
        binding.currentDate = viewModel.bindingDate()
    }

    override fun viewCreated() {
        initListener()
        setAdapter()
        bindingVm()
    }


    private fun initListener() {
        binding.selectSpaceImageEntrance.setOnClickListener(this)
        binding.selectSpaceImageLivingRoom.setOnClickListener(this)
        binding.selectSpaceImageBathroom.setOnClickListener(this)
        binding.selectSpaceImageOutside.setOnClickListener(this)
        binding.selectSpaceImageRoom.setOnClickListener(this)
        binding.selectSpaceImageKitchen.setOnClickListener(this)

        initViewEnabled()
        // go to 집안일 직접 추가 화면
        binding.selectSpaceGoDirectBtn.setOnClickListener {
            navigateToAddDirectTodoPage()
        }
        binding.selectSpaceGoDirectBtn2.setOnClickListener {
            navigateToAddDirectTodoPage()
        }

        // go to 다음 - 집안일 상세 화면
        binding.selectSpaceNextBtn.mainFooterButton.apply {
            text = resources.getString(R.string.next_btn_text)
            setOnClickListener {
                navigateToAddTodoPage2()
            }
        }

        // header 뒤로 가기
        binding.selectSpaceHeader.apply {
            defaultHeaderTitleTv.text = ""
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.selectSpaceCalender.setOnClickListener {
            createDatePickerDialog()
        }
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


    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.selectSpaceRecyclerview.layoutManager = gridLayoutManager
        binding.selectSpaceRecyclerview.addItemDecoration(VerticalItemDecorator(12))
        myAdapter = SelectSpaceChoreAdapter(emptyList<String>())
        binding.selectSpaceRecyclerview.adapter = myAdapter
        viewModel.setChoreAdapter(myAdapter)
    }

    private fun bindingVm() {
        viewModel.clearChore()
        lifecycleScope.launchWhenStarted {
            viewModel.choreList.collect {
                myAdapter = SelectSpaceChoreAdapter(viewModel.choreList.value)
                myAdapter.notifyDataSetChanged()
                binding.selectSpaceRecyclerview.adapter = myAdapter

                myAdapter.setItemClickListener(object :
                    SelectSpaceChoreAdapter.OnItemClickListener {
                    override fun onClick(v: View, chore: String, position: Int) {
                        v.isSelected = !v.isSelected
                        Timber.d("item click $position")
                        viewModel.updateChores(chore, v.isSelected)
                        viewModel.setIsSelectedChore(true)
                        if (viewModel.getChoreCount() == 0) {
                            viewModel.setIsSelectedChore(false)
                        }
                    }
                })
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.choreAdapter.collect {
                binding.selectSpaceRecyclerview.adapter = it
                viewModel.setIsSelectedChore(false)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.selectCalendar.collect {
                binding.selectSpaceCalender.text = viewModel.bindingDate()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.isConnectedNetwork = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isSelectedSpace.collect {
                binding.isSelectedSpace = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isSelectedChore.collect {
                binding.isSelectedChore = it
                binding.selectSpaceNextBtn.mainFooterButton.isEnabled = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.selectSpace.collect {
                when (it) {
                    "ENTRANCE" -> binding.selectedSpace = 1
                    "LIVINGROOM" -> binding.selectedSpace = 2
                    "BATHROOM" -> binding.selectedSpace = 3
                    "OUTSIDE" -> binding.selectedSpace = 4
                    "ROOM" -> binding.selectedSpace = 5
                    "KITCHEN" -> binding.selectedSpace = 6
                    else -> binding.selectedSpace = 0
                }

            }
        }
    }

    private fun navigateToAddDirectTodoPage() {
        binding.selectSpaceGoDirectBtn.findNavController()
            .navigate(
                SelectSpaceFragmentDirections.actionSelectSpaceFragmentToAddDirectTodoFragment(
                    viewType = ViewType.ADD,
                    selectDate = viewModel.selectCalendar.value,
                    houseWork = HouseWork(
                        assignees = arrayListOf(),
                        feedbackHouseworkResponse = null,
                        houseWorkCompleteId = -1,
                        houseWorkId = -1,
                        houseWorkName = "",
                        repeatCycle = RepeatCycle.ONCE.value,
                        repeatEndDate = "",
                        repeatPattern = "",
                        scheduledDate = "",
                        scheduledTime = null,
                        space = "",
                        success = false,
                        successDateTime = null
                    )
                )
            )
    }

    private fun navigateToAddTodoPage2() {
        findNavController().navigate(
            SelectSpaceFragmentDirections.actionSelectSpaceFragmentToAddHouseWorkFragment(
                SpaceChores(
                    spaceName = viewModel.selectSpace.value,
                    houseWorks = viewModel.chores.value,
                ), selectDate = viewModel.selectCalendar.value
            )
        )
    }

    private fun setDialog(space: View?) {
        val dialog = FairerDialog(requireContext(), DialogType.CHANGE)
        Timber.d("set dialog")
        dialog.showDialog()
        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                setSelected()
                onClick(space)
                viewModel.setIsSelectedChore(false)
                viewModel.clearChore()
            }
        }
    }

    override fun onClick(space: View?) {
        if (viewModel.isSelectedSpace.value) {
            if (viewModel.isSelectedChore.value) {
                if (viewModel.selectSpace.value == setSpaceNum(binding.selectedSpace))
                    setDialog(space)
            } else {
                setSelected()
                onClick(space)
            }
        } else {
            viewModel.setIsSelectedSpace(true)
            viewModel.setSpace("")
            when (space) {
                binding.selectSpaceImageEntrance -> {
                    viewModel.setSpace("ENTRANCE")
                    viewModel.setChoreList("ENTRANCE")
                }
                binding.selectSpaceImageLivingRoom -> {
                    viewModel.setSpace("LIVINGROOM")
                    viewModel.setChoreList("LIVINGROOM")
                }
                binding.selectSpaceImageBathroom -> {
                    viewModel.setSpace("BATHROOM")
                    viewModel.setChoreList("BATHROOM")
                }
                binding.selectSpaceImageOutside -> {
                    viewModel.setSpace("OUTSIDE")
                    viewModel.setChoreList("OUTSIDE")
                }
                binding.selectSpaceImageRoom -> {
                    viewModel.setSpace("ROOM")
                    viewModel.setChoreList("ROOM")
                }
                binding.selectSpaceImageKitchen -> {
                    viewModel.setSpace("KITCHEN")
                    viewModel.setChoreList("KITCHEN")
                }
            }
        }
    }

    private fun initViewEnabled() {
        binding.selectSpaceImageEntrance.isEnabled = true
        binding.selectSpaceImageLivingRoom.isEnabled = true
        binding.selectSpaceImageBathroom.isEnabled = true
        binding.selectSpaceImageOutside.isEnabled = true
        binding.selectSpaceImageRoom.isEnabled = true
        binding.selectSpaceImageKitchen.isEnabled = true
    }

    private fun setSelected() {
        binding.selectedSpace = 0
        viewModel.setIsSelectedSpace(false)
    }

    private fun setSpaceNum(num: Int): String {
        return when (num) {
            1 -> "ENTRANCE"
            2 -> "LIVINGROOM"
            3 -> "BATHROOM"
            4 -> "OUTSIDE"
            5 -> "ROOM"
            6 -> "KITCHEN"
            else -> ""
        }
    }


}