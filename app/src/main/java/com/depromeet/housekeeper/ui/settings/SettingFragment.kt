package com.depromeet.housekeeper.ui.settings

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.BuildConfig
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentSettingBinding
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by viewModels()

    override fun createView(binding: FragmentSettingBinding) {
        binding.vm = viewModel
    }

    override fun viewCreated() {
        initView()
        bindingVm()
        initListener()
    }

    private fun initView(){
        binding.version = viewModel.version.value
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    private fun bindingVm() {
        viewModel.setVersion(BuildConfig.VERSION_NAME)

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect{
                binding.layoutNetwork.isNetworkError = it
            }
        }
    }

    private fun initListener() {

        binding.settingHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.setting_header_title)

            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.accountRow.setOnClickListener {
            it.findNavController()
                .navigate(
                    SettingFragmentDirections.actionSettingFragmentToSettingProfileFragment(
                        null
                    )
                )
        }

        binding.spaceRow.setOnClickListener {
            it.findNavController()
                .navigate(SettingFragmentDirections.actionSettingFragmentToManageHouseFragment())
        }

        binding.alarmRow.setOnClickListener {
            it.findNavController().navigate(SettingFragmentDirections.actioinSettingFragmentToAlarmFragment())
        }

        binding.contactRow.setOnClickListener {
            it.findNavController()
                .navigate(SettingFragmentDirections.actionSettingFragmentToContactFragment())
        }

        binding.policyRow.setOnClickListener {
            it.findNavController()
                .navigate(SettingFragmentDirections.actionSettingFragmentToPolicyFragment())
        }

        binding.logoutRow.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val dialog = FairerDialog(requireContext(), DialogType.LOGOUT)
        dialog.showLogoutDialog()

        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                viewModel.signOut(requireContext())
                findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
            }
        }
    }
}
