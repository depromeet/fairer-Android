package com.depromeet.housekeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.depromeet.housekeeper.databinding.FragmentAddTodo1Binding
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog

class AddTodoFragment1 : Fragment() {
    lateinit var binding: FragmentAddTodo1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo1, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        
        initListener()

        return binding.root
    }
    
    private fun initListener() {
        // header title
//        binding.addTodo1Header.addTodoHeaderTv.text = "집안일 선택"
      
        binding.addTodo1Header.addTodoHeaderTb.setOnClickListener {
            val dialog = FairerDialog(requireContext())
            dialog.showDialog()
            dialog.onItemClickListener = object: FairerDialog.OnItemClickListener {
                override fun onItemClick() {
                    // ok btn click action
                }

            }
        }

        binding.addTodo1Header.addTodoBackBtn.setOnClickListener{
            navigateToMain()
        }


        // go to 집안일 직접 추가 화면
        binding.addTodo1GoDirectBtn.setOnClickListener {
            navigateToAddDirectTodoPage()
        }

        // go to 다음 - 집안일 상세 화면
        binding.addTodo1NextBtn.mainFooterButton.apply {
            text = resources.getString(R.string.next_btn_text)

            setOnClickListener {
                navigateToAddTodoPage2()
            }
        }

        // header 뒤로 가기
        binding.addTodo1Header.addTodoBackBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }

    }

    private fun navigateToAddDirectTodoPage() {
        binding.addTodo1GoDirectBtn.findNavController().navigate(R.id.action_addTodoFragment1_to_addDirectTodoFragment)
    }

    private fun navigateToAddTodoPage2() {
        binding.addTodo1NextBtn.mainFooterButton.findNavController().navigate(R.id.action_addTodoFragment1_to_addTodoFragment2)
    }
    private fun navigateToMain(){
        binding.addTodo1Header.addTodoBackBtn.findNavController().navigate(R.id.action_addTodoFragment1_to_mainFragment)
    }

}