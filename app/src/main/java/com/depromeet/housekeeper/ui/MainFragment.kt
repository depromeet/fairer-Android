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
import com.depromeet.housekeeper.adapter.HouseWorkAdapter
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.util.VerticalItemDecorator
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainFragment : Fragment() {

  lateinit var binding: FragmentMainBinding
  private lateinit var dayOfAdapter: DayOfWeekAdapter

  private var houseWorkAdapter: HouseWorkAdapter? = null
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
    setAdapter()
    bindingVm()
    setListener()
  }

  private fun setListener() {
    binding.btAddTodo.root.setOnClickListener {
      findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTodoFragment1(
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
  }

  private fun createDatePickerDialog() {
    val calendar = mainViewModel.getCalendar()
    val datePickerDialog = DatePickerDialog(
      this.requireContext(),
      { _, year, month, dayOfMonth ->
        //TODO("DayOfWeek Adapter 변경")
      },
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DAY_OF_MONTH) - 1,
    )
    datePickerDialog.show()

  }


  private fun setAdapter() {
    dayOfAdapter = DayOfWeekAdapter(mainViewModel.getCurrentWeek(),
      onClick = {
        mainViewModel.updateSelectDate(it)
      })
    binding.rvWeek.adapter = dayOfAdapter

    val list = mainViewModel.houseWorks.value?.houseWorks?.toMutableList() ?: mutableListOf()
    houseWorkAdapter = HouseWorkAdapter(list, onClick = {
      //TODO("집안일 수정 이동")
    }, {
      //TODO("집안일 완료하기 API 연동")
    }
    )
    binding.rvHouseWork.adapter = houseWorkAdapter
    binding.rvHouseWork.addItemDecoration(VerticalItemDecorator(20))
  }

  private fun bindingVm() {
    lifecycleScope.launchWhenStarted {
      mainViewModel.completeChoreNum.collect {
        val format = String.format(resources.getString(R.string.complete_chore), it)
        val spannerString = SpannableString(format).apply {
          setSpan(
            ForegroundColorSpan(Color.parseColor("#0C6DFF")),
            this.indexOf("일") + 1,
            binding.tvCompleteHouseChore.text.length - 1,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
          )
        }
        binding.tvCompleteHouseChore.text = spannerString
      }
    }

    lifecycleScope.launchWhenStarted {
      mainViewModel.houseWorks.collect { houseWork ->

        houseWork?.let {
          binding.isEmptyDone = it.countDone == 0
          binding.isEmptyRemain = it.countLeft == 0
          binding.tvRemainBadge.text = it.countLeft.toString()
          binding.tvEndBadge.text = it.countDone.toString()

          binding.layoutEmptyScreen.root.isVisible = houseWork.houseWorks.isEmpty()
          updateHouseWorkData(houseWork)
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      mainViewModel.currentState.collect {
        binding.isSelectDone = it == MainViewModel.CurrentState.DONE
        binding.isSelectRemain = it == MainViewModel.CurrentState.REMAIN

        mainViewModel.houseWorks.value?.let {
          updateHouseWorkData(it)
        }
      }
    }



    lifecycleScope.launchWhenStarted {
      mainViewModel.dayOfWeek.collect {
        val year = it.date.split("-")[0]
        val month = it.date.split("-")[1]
        binding.tvMonth.text = "${year}년 ${month}월"
      }
    }
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
    val datePattern = "HH:MM"
    val format = SimpleDateFormat(datePattern, Locale.getDefault())

    val lastIndex = list.indexOfLast {
      !it.success && it.scheduledTime < format.format(Calendar.getInstance().time)
    }
    list.add(lastIndex + 1, list[lastIndex].copy(now = 1))
    houseWorkAdapter?.updateDate(list)
  }
}
