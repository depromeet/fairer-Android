package com.depromeet.housekeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.depromeet.housekeeper.databinding.FragmentAddTodo1Binding

class AddTodoFragment1 : Fragment() {
    lateinit var binding: FragmentAddTodo1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo1, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        
        initListener()

        return binding.root
    }
    
    private fun initListener() {
        binding.addTodo1Header.addTodoHeaderTv.text = "집안일 선택"
        
        binding.addTodo1NextBtn.setOnClickListener {
            navigateToAddTodoPage1()
        }

        binding.addTodo1Header.addTodoBackBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    private fun navigateToAddTodoPage1() {
        binding.addTodo1NextBtn.findNavController().navigate(R.id.action_addTodoFragment1_to_addTodoFragment2)
    }

}