package com.depromeet.housekeeper.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentManageHouseBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import timber.log.Timber

class ManageHouseFragment : Fragment() {
    lateinit var binding: FragmentManageHouseBinding
    private val viewModel: ManageHouseViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_manage_house, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        initListener()
        bindingVm()

        return binding.root
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