package com.depromeet.housekeeper.ui.settings

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    val viewModel: AlarmViewModel by viewModels()
    override fun createView(binding: FragmentAlarmBinding) {
        binding.alarmHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
            defaultHeaderTitleTv.text = getString(R.string.setting_alarm_row_text)
        }
        binding.alarmHouseworkSwitch.setOnCheckedChangeListener{_, isChecked ->
                viewModel.setAlarm(0,isChecked)
        }
        binding.alarmRemainSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAlarm(1,isChecked)
        }
    }

    override fun viewCreated() {
        bindingVm()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.alarmInfo.collectLatest { alarmInfo ->
                if (alarmInfo != null) {
                    binding.alarmHouseworkSwitch.isChecked = alarmInfo.scheduledTimeStatus
                    binding.alarmRemainSwitch.isChecked = alarmInfo.notCompleteStatus
                }
            }
        }
    }
}
