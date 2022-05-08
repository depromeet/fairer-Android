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
import com.depromeet.housekeeper.model.HouseWork
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.Calendar

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
  }

  private fun createDatePickerDialog() {
    val calendar = mainViewModel.getCalendar()
    val datePickerDialog = DatePickerDialog(
      this.requireContext(),
      { _, year, month, dayOfMonth ->
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
    houseWorkAdapter = HouseWorkAdapter(list) {

    }
    binding.rvHouseWork.adapter = houseWorkAdapter

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
          binding.layoutEmptyScreen.root.isVisible = houseWork.houseWorks.isEmpty()
          houseWorkAdapter?.updateDate(houseWork.houseWorks.toMutableList())
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
}
