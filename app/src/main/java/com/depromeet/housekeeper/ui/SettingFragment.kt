package com.depromeet.housekeeper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.BuildConfig
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding:FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingVm()
        initListener()
    }

    private fun bindingVm() {
        viewModel.setVersion(BuildConfig.VERSION_NAME)
        binding.version = viewModel.version.value
    }

    private fun initListener() {

        binding.settingHeader.apply {
            addTodoHeaderTv.text = resources.getString(R.string.setting_header_title)

            addTodoBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.logoutRow.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToLoginFragment())
        }
    }

}