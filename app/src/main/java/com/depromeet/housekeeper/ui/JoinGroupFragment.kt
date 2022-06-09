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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentJoinGroupBinding

class JoinGroupFragment : Fragment() {
    lateinit var binding : FragmentJoinGroupBinding
    private val navArgs by navArgs<JoinGroupFragmentArgs>()
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
        binding.joinGroupHeader.addTodoBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.joinGroupMakeSpaceButton.setOnClickListener {
            findNavController().navigate(R.id.action_joinGroupFragment_to_mainFragment)
        }

    }
    private fun spannable(){
        binding.joinGroupMainText.text = getString(R.string.join_group_main_text, navArgs.name)
        val spanText = SpannableStringBuilder(binding.joinGroupMainText.text)
        spanText.apply{
            setSpan(ForegroundColorSpan(resources.getColor(R.color.highlight)),
                6,6+navArgs.name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.joinGroupMainText.text = spanText
    }
}