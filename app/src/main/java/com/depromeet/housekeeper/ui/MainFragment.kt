package com.depromeet.housekeeper.ui

import android.app.DatePickerDialog
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.depromeet.housekeeper.adapter.DayOfWeekAdapter
import com.depromeet.housekeeper.adapter.GroupProfileAdapter
import com.depromeet.housekeeper.adapter.HouseWorkAdapter
import com.depromeet.housekeeper.adapter.MainSectionAdapter
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.AssigneeSelect
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.SectionHouseWorks
import com.depromeet.housekeeper.model.enums.ViewType
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var dayOfAdapter: DayOfWeekAdapter
    private var houseWorkAdapter: HouseWorkAdapter? = null
    private lateinit var sectionAdapter: MainSectionAdapter
    private lateinit var groupProfileAdapter: GroupProfileAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = mainViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.apply {
            getRules()
            getGroupName()
            updateSelectDate(getToday())
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
        /*binding.tvRemain.setOnClickListener {
            mainViewModel.updateState(MainViewModel.CurrentState.REMAIN)
        }
        binding.tvEnd.setOnClickListener {
            mainViewModel.updateState(MainViewModel.CurrentState.DONE)
        }*/
        binding.mainHeader.mainHeaderSettingIv.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingFragment())
        }
        binding.lvRule.root.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRuleFragment())
        }
        binding.tvToday.setOnClickListener {
            dayOfAdapter.updateDate(getCurrentWeek())
            mainViewModel.updateSelectDate(mainViewModel.getToday())
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
        dayOfAdapter = DayOfWeekAdapter(getCurrentWeek(),
            onClick = {
                mainViewModel.updateSelectDate(it)
            })
        val animator: RecyclerView.ItemAnimator? = binding.rvWeek.itemAnimator
        if (animator is SimpleItemAnimator) {
            (animator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        val gridLayoutManager = GridLayoutManager(context, 7)
        binding.rvWeek.layoutManager = gridLayoutManager
        binding.rvWeek.adapter = dayOfAdapter
        rvWeekSwipeListener()

        val list =
            mainViewModel.selectHouseWorks.value?.houseWorks?.toMutableList() ?: mutableListOf()
        Timber.d("$list")
        houseWorkAdapter = HouseWorkAdapter(list, onClick = {
            it
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
        val sectionTitle =
            listOf(SectionHouseWorks(null, list), SectionHouseWorks("끝낸 집안일", list)).toMutableList()
        sectionAdapter = MainSectionAdapter(sectionTitle, houseWorkAdapter)
        binding.rvHouseWork.adapter = sectionAdapter

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
                dayOfAdapter.updateLeftCntMap(mainViewModel.weekendChoresLeft)
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.selectHouseWorks.collect { houseWorks ->
                Timber.d("collect \n$houseWorks")
                houseWorks?.let {
                    binding.layoutDoneScreen.root.isVisible =
                        it.countLeft == 0 && it.countDone > 0
                    binding.layoutEmptyScreen.root.isVisible =
                        (it.countLeft == 0 && it.countDone == 0)

                    //binding.tvRemainBadge.text = it.countLeft.toString()
                    // dayOfAdapter.updateChoreSize(it.countLeft)//TODO 나중에 api 연결할때 수정 -> 필요없으면 삭제
                    //binding.tvEndBadge.text = it.countDone.toString()

                    binding.layoutEmptyScreen.root.isVisible = it.houseWorks.isEmpty()
                    updateHouseWorkData(it)
                    it.houseWorks.forEach {
                        mainViewModel.getDetailHouseWork(it.houseWorkId)
                    }
                }
            }
        }

        /* lifecycleScope.launchWhenStarted {
             mainViewModel.currentState.collect {
                 val houseWork = mainViewModel.selectHouseWork.value ?: return@collect
                 binding.layoutDoneScreen.root.isVisible =
                     it == MainViewModel.CurrentState.REMAIN && (houseWork.countLeft == 0 && houseWork.countDone > 0)

                 mainViewModel.selectHouseWork.value?.let {
                     updateHouseWorkData(it)
                 }
             }
         }*/

        lifecycleScope.launchWhenStarted {
            mainViewModel.dayOfWeek.collect {
                val year = it.date.split("-")[0]
                val month = it.date.split("-")[1]
                binding.tvMonth.text = "${year}년 ${month}월"
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


        houseWorkAdapter?.updateDate(remainList)
    }

    private fun getCurrentWeek(): MutableList<DayOfWeek> {
        val format = SimpleDateFormat("yyyy-MM-dd-EEE", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        val days = mutableListOf<String>()
        days.add(format.format(calendar.time))
        repeat(6) {
            calendar.add(Calendar.DATE, 1)
            days.add(format.format(calendar.time))
        }
        return days.map {
            DayOfWeek(
                date = it,
                isSelect = it == format.format(Calendar.getInstance().time)
            )
        }.toMutableList()
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