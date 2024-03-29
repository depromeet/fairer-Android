package com.depromeet.housekeeper.ui.join

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentJoinGroupBinding
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinGroupFragment : BaseFragment<FragmentJoinGroupBinding>(R.layout.fragment_join_group) {

    override fun createView(binding: FragmentJoinGroupBinding) {
    }

    override fun viewCreated() {
        initListener()
        spannable()
    }


    private fun initListener() {
        binding.joinGroupHeader.defaultHeaderTitleTv.text = ""
        binding.joinGroupHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.joinGroupMakeSpaceButton.setOnClickListener {
            findNavController().navigate(
                JoinGroupFragmentDirections.actionJoinGroupFragmentToSignNameFragment(
                    SignViewType.GroupName,
                    null
                )
            )
        }
        binding.joinGroupJoinSpaceButton.setOnClickListener {
            findNavController().navigate(
                JoinGroupFragmentDirections.actionJoinGroupFragmentToSignNameFragment(
                    SignViewType.InviteCode,
                    null
                )
            )
        }

    }

    private fun spannable() {
        binding.joinGroupMainText.text =
            getString(R.string.join_group_main_text, PrefsManager.userName)
        val spanText = SpannableStringBuilder(binding.joinGroupMainText.text)
        spanText.apply {
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.highlight)),
                6, 6 + PrefsManager.userName.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.joinGroupMainText.text = spanText
    }


}