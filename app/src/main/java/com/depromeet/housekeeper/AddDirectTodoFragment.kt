package com.depromeet.housekeeper

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.housekeeper.adapter.DayRepeatAdapter
import com.depromeet.housekeeper.databinding.FragmentAddDirectTodoBinding
import java.util.Calendar

class AddDirectTodoFragment : Fragment() {
    lateinit var binding: FragmentAddDirectTodoBinding
    lateinit var imm: InputMethodManager
    lateinit var dayRepeatAdapter: DayRepeatAdapter
    private val addDirectTodoViewModel: AddDirectTodoViewModel by viewModels()
    private val navArgs by navArgs <AddDirectTodoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_direct_todo, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = addDirectTodoViewModel
        binding.currentDate = "${navArgs.selectDate.date}요일"

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

        binding.addDirectTodoDateTv.setOnClickListener {
            createDatePickerDialog()
        }
    }

    private fun createDatePickerDialog() {
        val selectDate = navArgs.selectDate.date

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectDate.split("-")[0].toInt())
            set(Calendar.MONTH,selectDate.split("-")[1].toInt())
            set(Calendar.DAY_OF_MONTH,selectDate.split("-")[2].toInt())
        }

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->
                addDirectTodoViewModel.updateCalendarView(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH)-1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()
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