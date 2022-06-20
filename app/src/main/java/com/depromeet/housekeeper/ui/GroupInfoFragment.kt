package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.UserInfoAdapter
import com.depromeet.housekeeper.databinding.FragmentGroupInfoBinding
import com.depromeet.housekeeper.util.VerticalItemDecorator
import kotlinx.coroutines.flow.collect

class GroupInfoFragment : Fragment() {
    lateinit var binding: FragmentGroupInfoBinding
    private val viewModel: GroupInfoViewModel by viewModels()
    private lateinit var myAdapter: UserInfoAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_info, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingVm()
        setAdapter()
        initListener()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.groupName.collect {
                binding.groupInfoText.text = getString(R.string.group_info_text, viewModel.groupName.value)
                spannable()
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
        val dummyListProfile: List<String> = listOf(
            "https://i.pinimg.com/originals/61/0b/12/610b12fdc6afe3beafd439b43a52ad24.png",
            "https://www.urbanbrush.net/web/wp-content/uploads/edd/2020/11/urbanbrush-20201104103659627968.jpg"
        )
        val dummyListName: List<String> = listOf(
            "박정준", "홍길동"
        )
        binding.groupInfoRecyclerImageview.layoutManager = gridLayoutManager
        myAdapter = UserInfoAdapter(dummyListProfile,dummyListName)
        binding.groupInfoRecyclerImageview.adapter = myAdapter
        binding.groupInfoRecyclerImageview.addItemDecoration(VerticalItemDecorator(16))
    }

    private fun spannable(){
        val spanText = SpannableStringBuilder(binding.groupInfoText.text)
        spanText.apply{
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.highlight)),
                0,viewModel.groupName.value.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.groupInfoText.text = spanText
    }
}