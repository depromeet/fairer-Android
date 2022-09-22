package com.depromeet.housekeeper.ui.settings

import android.widget.ScrollView
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentPolicyBinding

class PolicyFragment : BaseFragment<FragmentPolicyBinding>(R.layout.fragment_policy) {

    override fun createView(binding: FragmentPolicyBinding) {
    }

    override fun viewCreated() {
        initListener()
    }


    private fun initListener() {
        binding.policyScrollview.fullScroll(ScrollView.FOCUS_DOWN)
        binding.policyHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.policy_header_title)
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }
    }

}