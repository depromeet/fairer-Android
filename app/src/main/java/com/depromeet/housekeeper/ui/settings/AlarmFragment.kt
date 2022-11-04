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
    private val viewModel: AlarmViewModel by viewModels()

    override fun createView(binding: FragmentAlarmBinding) {
    }

    override fun viewCreated() {
        initListener()
        bindingVm()
    }


    private fun initListener() {
        binding.alarmHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
            defaultHeaderTitleTv.text = getString(R.string.setting_alarm_row_text)
        }
        binding.alarmHouseworkSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                viewModel.setBasicAlarm(true)
            }else{
                viewModel.setBasicAlarm(false)
            }
        }
        binding.alarmRemainSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                viewModel.setRemindAlarm(true)
            }else{
                viewModel.setRemindAlarm(false)
            }
        }
    }
    private fun bindingVm(){
        lifecycleScope.launchWhenCreated {
            viewModel.basicAlarm.collectLatest {
                binding.completeAlarm = it
                viewModel.setAlarmInformation(it,viewModel.remindAlarm.value)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.remindAlarm.collectLatest {
                binding.remindAlarm = it
                viewModel.setAlarmInformation(viewModel.basicAlarm.value,it)
            }
        }

    }



}