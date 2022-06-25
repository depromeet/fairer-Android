package com.depromeet.housekeeper.ui

import android.app.DatePickerDialog
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
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.DayOfWeekAdapter
import com.depromeet.housekeeper.adapter.GroupProfileAdapter
import com.depromeet.housekeeper.adapter.HouseWorkAdapter
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.util.VerticalItemDecorator
import kotlinx.coroutines.flow.collect

class MainFragment : Fragment() {

  lateinit var binding: FragmentMainBinding
  private lateinit var dayOfAdapter: DayOfWeekAdapter
  private var houseWorkAdapter: HouseWorkAdapter? = null
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
    binding.btAddTodo.root.setOnClickListener {
      findNavController().navigate(MainFragmentDirections.actionMainFragmentToSelectSpaceFragment(
        mainViewModel.dayOfWeek.value))
    }

    binding.ivLeft.setOnClickListener {
      dayOfAdapter.updateDate(mainViewModel.getLastWeek())
    }

    binding.ivRignt.setOnClickListener {
      dayOfAdapter.updateDate(mainViewModel.getNextWeek())
    }

    binding.tvMonth.setOnClickListener {
      createDatePickerDialog()
    }

    binding.tvRemain.setOnClickListener {
      mainViewModel.updateState(MainViewModel.CurrentState.REMAIN)
    }

    binding.tvEnd.setOnClickListener {
      mainViewModel.updateState(MainViewModel.CurrentState.DONE)
    }

    binding.mainHeader.mainHeaderSettingIv.setOnClickListener {
      findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingFragment())
    }

    binding.lvRule.root.setOnClickListener {
      findNavController().navigate(MainFragmentDirections.actionMainFragmentToRuleFragment())
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
    dayOfAdapter = DayOfWeekAdapter(mainViewModel.getCurrentWeek(),
      onClick = {
        mainViewModel.updateSelectDate(it)
      })
    binding.rvWeek.adapter = dayOfAdapter

    val list = mainViewModel.selectHouseWork.value?.houseWorks?.toMutableList() ?: mutableListOf()
    houseWorkAdapter = HouseWorkAdapter(list, onClick = {
      it
      val dayOfWeek = DayOfWeek(it.scheduledDate, false)
      findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddDirectTodoFragment(
        viewType = ViewType.EDIT, houseWork = it, selectDate = mainViewModel.dayOfWeek.value))
    }, {
      mainViewModel.updateChoreState(it.houseWorkId)
    }
    )
    binding.rvHouseWork.adapter = houseWorkAdapter
    binding.rvHouseWork.addItemDecoration(VerticalItemDecorator(20))


    groupProfileAdapter = GroupProfileAdapter(mainViewModel.groups.value.toMutableList()) {

    }
    binding.rvGroups.adapter = groupProfileAdapter
  }

  private fun bindingVm() {
    lifecycleScope.launchWhenStarted {
      mainViewModel.completeChoreNum.collect {
        val completeFormat = String.format(resources.getString(R.string.complete_chore), it)
        binding.tvCompleteHouseChore.text =
          getSpannableText(
            completeFormat,
            completeFormat.indexOf("에") + 1,
            completeFormat.indexOf("나")
          )
      }
    }

    lifecycleScope.launchWhenCreated {
      mainViewModel.selectHouseWork.collect { houseWork ->

        houseWork?.let {
          binding.isEmptyDone = it.countDone == 0
          binding.isEmptyRemain = it.countLeft == 0

          binding.layoutDoneScreen.root.isVisible =
            mainViewModel.currentState.value == MainViewModel.CurrentState.REMAIN && it.countLeft == 0 && it.countDone > 0
          binding.layoutEmptyScreen.root.isVisible = (it.countLeft == 0 && it.countDone == 0)

          binding.tvRemainBadge.text = it.countLeft.toString()
          binding.tvEndBadge.text = it.countDone.toString()

          binding.layoutEmptyScreen.root.isVisible = houseWork.houseWorks.isEmpty()
          updateHouseWorkData(houseWork)
        }
      }
    }

    lifecycleScope.launchWhenCreated {
      mainViewModel.currentState.collect {
        val houseWork = mainViewModel.selectHouseWork.value ?: return@collect
        binding.isSelectDone = it == MainViewModel.CurrentState.DONE
        binding.isSelectRemain = it == MainViewModel.CurrentState.REMAIN
        binding.layoutDoneScreen.root.isVisible =
          it == MainViewModel.CurrentState.REMAIN && (houseWork.countLeft == 0 && houseWork.countDone > 0)

        mainViewModel.selectHouseWork.value?.let {
          updateHouseWorkData(it)
        }
      }
    }

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
        groupProfileAdapter.updateDate(it.toMutableList())
      }
    }

    lifecycleScope.launchWhenStarted {
      mainViewModel.rule.collect {
        binding.lvRule.rule = it
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
    val list = when (mainViewModel.currentState.value) {
      MainViewModel.CurrentState.REMAIN -> {
        houseWork.houseWorks
          .filter { !it.success }
          .sortedBy { it.scheduledTime }
          .toMutableList()
      }
      else -> {
        houseWork.houseWorks
          .filter { it.success }
          .sortedBy { it.scheduledTime }
          .toMutableList()
      }
    }
    houseWorkAdapter?.updateDate(list)
  }
}
