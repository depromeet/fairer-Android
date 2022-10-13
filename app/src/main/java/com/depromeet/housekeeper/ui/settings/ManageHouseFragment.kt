package com.depromeet.housekeeper.ui.settings

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentManageHouseBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManageHouseFragment : BaseFragment<FragmentManageHouseBinding>(R.layout.fragment_manage_house) {
    private val viewModel: ManageHouseViewModel by viewModels()

    override fun createView(binding: FragmentManageHouseBinding) {
    }

    override fun viewCreated() {
        initView()
        initListener()
        bindingVm()
    }

    private fun initView(){
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }


    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.response.collect {
                if (it) {
                    Timber.d("leave Team response : $it")
                    findNavController().navigate(ManageHouseFragmentDirections.actionManageHouseFragmentToJoinGroupFragment())
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect{
                binding.layoutNetwork.isNetworkError = it
            }
        }
    }

    private fun initListener() {
        binding.manageHouseHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.manage_house_header_title)

            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.renameHouseRow.setOnClickListener {
            it.findNavController().navigate(
                ManageHouseFragmentDirections.actionManageHouseFragmentToSignNameFragment(
                    viewType = SignViewType.ModifyGroupName,
                    code = null
                )
            )
        }

        binding.inviteRow.setOnClickListener {
            it.findNavController().navigate(
                ManageHouseFragmentDirections.actionManageHouseFragmentToInviteFragment(
                    InviteViewType.SETTING,
                    null
                )
            )
        }

        binding.exitHouseRow.setOnClickListener {
            setDialog()
        }

    }

    private fun setDialog() {
        val dialog = FairerDialog(requireContext(), DialogType.EXIT)
        Timber.d("set dialog")
        dialog.showDialog()
        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                viewModel.leaveTeam()
                Timber.d("leave Team")
            }
        }
    }


}