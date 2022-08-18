package com.depromeet.housekeeper.ui.join

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentJoinGroupBinding
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.util.NAVIGATION_CODE
import com.depromeet.housekeeper.util.NAVIGATION_VIEW_TYPE
import com.depromeet.housekeeper.util.NavigationUtil.navigateSafe

class JoinGroupFragment : Fragment() {
    lateinit var binding : FragmentJoinGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_group, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        spannable()
    }

    private fun initListener() {
        binding.joinGroupHeader.defaultHeaderTitleTv.text = ""
        binding.joinGroupHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.joinGroupMakeSpaceButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(NAVIGATION_VIEW_TYPE, SignViewType.GroupName)
                putString(NAVIGATION_CODE, null)
            }
            findNavController().navigateSafe(R.id.action_joinGroupFragment_to_signNameFragment, bundle)
        }
        binding.joinGroupJoinSpaceButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(NAVIGATION_VIEW_TYPE, SignViewType.InviteCode)
                putString(NAVIGATION_CODE, null)
            }
            findNavController().navigateSafe(R.id.action_joinGroupFragment_to_signNameFragment, bundle)
        }

    }

    private fun spannable(){
        binding.joinGroupMainText.text = getString(R.string.join_group_main_text, PrefsManager.userName)
        val spanText = SpannableStringBuilder(binding.joinGroupMainText.text)
        spanText.apply{
            setSpan(ForegroundColorSpan(resources.getColor(R.color.highlight)),
                6,6+ PrefsManager.userName.length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.joinGroupMainText.text = spanText
    }
}