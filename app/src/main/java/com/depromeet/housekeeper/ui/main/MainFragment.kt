package com.depromeet.housekeeper.ui.main

import android.app.DatePickerDialog
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.model.AssigneeSelect
import com.depromeet.housekeeper.model.response.HouseWorks
import com.depromeet.housekeeper.model.enums.HouseWorkState
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.ui.main.adapter.DayOfWeekAdapter
import com.depromeet.housekeeper.ui.main.adapter.GroupProfileAdapter
import com.depromeet.housekeeper.ui.main.adapter.HouseWorkAdapter
import com.depromeet.housekeeper.util.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var dayOfAdapter: DayOfWeekAdapter
    private var houseWorkAdapter: HouseWorkAdapter? = null
    private lateinit var groupProfileAdapter: GroupProfileAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun createView(binding: FragmentMainBinding) {
        binding.vm = mainViewModel
    }

    override fun viewCreated() {
        mainViewModel.apply {
            getRules()
            getGroupName()
            updateSelectDate(DateUtil.getTodayFull())
            updateStartDateOfWeek(DateUtil.getCurrentStartDate())
        }

        initView()
        setAdapter()
        bindingVm()
        setListener()
    }


    private fun initView() {
        val userNameFormat =
            String.format(resources.getString(R.string.user_name), PrefsManager.userName)
        binding.tvName.text = getSpannableText(userNameFormat, 0, userNameFormat.indexOf("님"))
    }

    private fun setListener() {
        binding.btAddTodo.addChoreView.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSelectSpaceFragment(
                    mainViewModel.dayOfWeek.value
                )
            )
        }
        binding.tvMonth.setOnClickListener {
            createDatePickerDialog()
        }

        binding.mainHeader.mainHeaderSettingIv.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingFragment())
        }

        binding.lvRule.root.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRuleFragment())
        }
        binding.tvToday.setOnClickListener {
            dayOfAdapter.updateDate(DateUtil.getCurrentWeek())
            mainViewModel.updateSelectDate(DateUtil.getTodayFull())
            mainViewModel.updateStartDateOfWeek(DateUtil.getCurrentStartDate())
        }

    }

    private fun createDatePickerDialog() {
        val currentDate = mainViewModel.dayOfWeek.value

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->
                //TODO("DayOfWeek Adapter 변경")
                val list = mainViewModel.getDatePickerWeek(year, month, dayOfMonth)
                dayOfAdapter.updateDate(list)
            },
            currentDate.date.split("-")[0].toInt(),
            currentDate.date.split("-")[1].toInt() - 1,
            currentDate.date.split("-")[2].toInt(),
        )
        datePickerDialog.show()

    }

    private fun setAdapter() {
        dayOfAdapter = DayOfWeekAdapter(DateUtil.getCurrentWeek(),
            onClick = {
                mainViewModel.updateSelectDate(it)
            })
        val animator: RecyclerView.ItemAnimator? = binding.rvWeek.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        val gridLayoutManager = GridLayoutManager(context, 7)
        binding.rvWeek.layoutManager = gridLayoutManager
        binding.rvWeek.adapter = dayOfAdapter
        rvWeekSwipeListener()

        val list =
            mainViewModel.selectHouseWorks.value?.houseWorks?.toMutableList() ?: mutableListOf()
        houseWorkAdapter = HouseWorkAdapter(list, onClick = {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToAddDirectTodoFragment(
                    viewType = ViewType.EDIT,
                    houseWork = it,
                    selectDate = mainViewModel.dayOfWeek.value
                )
            )
        }, {
            mainViewModel.updateChoreState(it)
        }
        )
        binding.rvHouseWork.adapter = houseWorkAdapter
        binding.rvHouseWork.addItemDecoration(VerticalItemDecorator(20))
        val swipeHelperCallback = SwipeHelperCallback(houseWorkAdapter!!).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 5)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvHouseWork)
        binding.rvHouseWork.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.rvHouseWork)
            false
        }
        groupProfileAdapter = GroupProfileAdapter(mainViewModel.groups.value.toMutableList()) {
            mainViewModel.updateSelectUser(it.memberId)
        }
        binding.rvGroups.adapter = groupProfileAdapter
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.completeChoreNum.collect {
                when (it) {
                    0 -> {
                        binding.tvCompleteHouseChore.text = getString(R.string.complete_chore_yet)
                    }
                    else -> {
                        val completeFormat =
                            String.format(resources.getString(R.string.complete_chore), it)
                        binding.tvCompleteHouseChore.text =
                            getSpannableText(
                                completeFormat,
                                completeFormat.indexOf("에") + 1,
                                completeFormat.indexOf("나")
                            )
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.weekendHouseWorks.collect {
                Timber.d("$MAIN_TAG : weekendHouseWorks : ${it.keys}")
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.weekendChoresLeft.collect {
                Timber.d("$MAIN_TAG : weekendChoresLeft : ${it}")
                dayOfAdapter.updateLeftCnt(mainViewModel.weekendChoresLeft.value)
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.selectHouseWorks.collect {
                Timber.d("$MAIN_TAG collect \n$it")
                if (it != null) {
                    if (it.countLeft == 0) {
                        if (it.countDone > 0) {
                            binding.houseworkState = HouseWorkState.DONE
                        } else if (it.countDone == 0) {
                            binding.houseworkState = HouseWorkState.EMPTY
                        }
                    } else if (it.countLeft > 0) {
                        binding.houseworkState = HouseWorkState.LEFT
                    }

                    updateHouseWorkData(it)
                    it.houseWorks.forEach {
                        mainViewModel.getDetailHouseWork(it.houseWorkId)
                    }
                } else {
                    binding.houseworkState = HouseWorkState.EMPTY
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.dayOfWeek.collect {
                Timber.d("$MAIN_TAG : selectedDate ${it}")
                mainViewModel.updateSelectHouseWork(it.date.substring(0, 10))
                val year = it.date.split("-")[0]
                val month = it.date.split("-")[1]
                binding.tvMonth.text = "${year}년 ${month}월"
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.startDateOfWeek.collect {
                Timber.d("$MAIN_TAG : startDateOfWeek $it")
                mainViewModel.getHouseWorks()
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.networkError.collect {
                binding.isConnectedNetwork = it
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.groupName.collect {
                binding.tvGroupName.text = it
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.groups.collect {
                val profileGroups: List<AssigneeSelect> = it.map {
                    AssigneeSelect(
                        it.memberId,
                        it.memberName,
                        it.profilePath,
                        it.memberId == mainViewModel.selectUserId.value
                    )
                }
                groupProfileAdapter.updateDate(profileGroups.toMutableList())
            }
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.rule.collect {
                binding.lvRule.rule = it
            }
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.selectUserId.collect {
                mainViewModel.getHouseWorks()
            }
        }
    }

    private fun getSpannableText(format: String, firstIndex: Int, lastIndex: Int): SpannableString {
        val spannerString2 = SpannableString(format).apply {
            setSpan(
                ForegroundColorSpan(Color.parseColor("#0C6DFF")),
                firstIndex,
                lastIndex,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannerString2
    }

    private fun updateHouseWorkData(houseWork: HouseWorks) {
        val remainList =
            houseWork.houseWorks
                .filter { !it.success }
                .sortedBy { it.scheduledTime }
                .toMutableList()

        val doneList =
            houseWork.houseWorks
                .filter { it.success }
                .sortedBy { it.scheduledTime }
                .toMutableList()

        houseWorkAdapter?.updateDate(remainList, doneList)
    }


    private fun rvWeekSwipeListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ACTION_STATE_SWIPE) {
                    val view = binding.rvWeek
                    val newX = 0.0 // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        view,
                        newX.toFloat(),
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> dayOfAdapter.updateDate(mainViewModel.getNextWeek())
                    ItemTouchHelper.RIGHT -> dayOfAdapter.updateDate(mainViewModel.getLastWeek())
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvWeek)
    }


}