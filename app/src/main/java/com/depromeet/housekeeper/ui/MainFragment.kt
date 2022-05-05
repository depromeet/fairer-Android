package com.depromeet.housekeeper.ui

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
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class MainFragment : Fragment() {

  lateinit var binding: FragmentMainBinding
  lateinit var adapter: DayOfWeekAdapter
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
      findNavController().navigate(R.id.action_mainFragment_to_addTodoFragment1)
    }

    binding.ivLeft.setOnClickListener {
      adapter.updateDate(mainViewModel.getLastWeek())
    }

    binding.ivRignt.setOnClickListener {
      adapter.updateDate(mainViewModel.getNextWeek())
    }
  }

  private fun setAdapter() {
    adapter = DayOfWeekAdapter(mainViewModel.getCurrentWeek(),
      onClick = {
        mainViewModel.updateSelectDate(it.date.split("-")[1])
      })
    binding.rvWeek.adapter = adapter
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
      mainViewModel.houseWorks.collect {
        //TODO adapter list 연결
      }
    }

  }
}
