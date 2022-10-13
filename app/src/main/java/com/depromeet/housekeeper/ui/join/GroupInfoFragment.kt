package com.depromeet.housekeeper.ui.join

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentGroupInfoBinding
import com.depromeet.housekeeper.ui.join.adapter.UserInfoAdapter
import com.depromeet.housekeeper.util.VerticalItemDecorator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupInfoFragment : BaseFragment<FragmentGroupInfoBinding>(R.layout.fragment_group_info) {
    private val viewModel: GroupInfoViewModel by viewModels()
    private lateinit var myAdapter: UserInfoAdapter

    override fun createView(binding: FragmentGroupInfoBinding) {
    }

    override fun viewCreated() {
        initView()
        bindingVm()
        setAdapter()
        initListener()
    }

    private fun initView() {
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.groupName.collect {
                binding.groupInfoText.text =
                    getString(R.string.group_info_text, viewModel.groupName.value)
                spannable()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.groups.collect {
                setAdapter()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect{
                binding.layoutNetwork.isNetworkError = it
            }
        }
    }

    private fun initListener() {
        binding.groupInfoNextBtn.mainFooterButton.isEnabled = true
        binding.groupInfoNextBtn.mainFooterButton.setText(R.string.group_info_next_button)
        binding.groupInfoText.text = getString(R.string.group_info_text, viewModel.groupName.value)
        spannable()
        binding.groupInfoNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(R.id.action_groupInfoFragment_to_mainFragment)
        }
    }

    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 4)
        binding.groupInfoRecyclerImageview.layoutManager = gridLayoutManager
        myAdapter = UserInfoAdapter(viewModel.groups.value)
        binding.groupInfoRecyclerImageview.adapter = myAdapter
        binding.groupInfoRecyclerImageview.addItemDecoration(VerticalItemDecorator(16))
    }

    private fun spannable() {
        val spanText = SpannableStringBuilder(binding.groupInfoText.text)
        spanText.apply {
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.highlight)),
                0, viewModel.groupName.value.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.groupInfoText.text = spanText
    }


}