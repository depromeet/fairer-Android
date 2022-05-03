package com.depromeet.housekeeper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddDirectTodoBinding

class AddDirectTodoFragment : Fragment() {
    lateinit var binding: FragmentAddDirectTodoBinding
    lateinit var imm: InputMethodManager
    lateinit var dayRepeatAdapter: DayRepeatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_direct_todo, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initListener()
        setAdapter()

        return binding.root
    }

    private fun initListener() {
        binding.addDirectTodoBackgroundCl.setOnClickListener {
            hideKeyboard(binding.addDirectTodoTitleEt)
        }

        binding.addDirectTodoHeader.apply {
            addTodoBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
            addTodoHeaderTv.text = ""
        }

        binding.addDirectTodoDoneBtn.mainFooterButton.apply {
            text = resources.getString(R.string.add_todo_done_btn_txt)

            setOnClickListener {
                it.findNavController().navigate(R.id.action_addDirectTodoFragment_to_mainFragment)
            }
        }
    }

    private fun setAdapter() {
        // 요일 반복 rv adapter
        val days: Array<String> = resources.getStringArray(R.array.day_array)
        dayRepeatAdapter = DayRepeatAdapter(days)
        binding.addDirectTodoRepeatRv.adapter = dayRepeatAdapter
    }

    private fun hideKeyboard(v: View) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }


}