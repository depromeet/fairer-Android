package com.depromeet.housekeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.depromeet.housekeeper.databinding.FragmentAddTodo1Binding
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import timber.log.Timber

class AddTodoFragment1 : Fragment(),View.OnClickListener {
    lateinit var binding: FragmentAddTodo1Binding
    private var selected:Boolean= false

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
        binding.addTodo1ImageEntrance.setOnClickListener(this)
        binding.addTodo1ImageLivingRoom.setOnClickListener(this)
        binding.addTodo1ImageBathroom.setOnClickListener(this)
        binding.addTodo1ImageOutside.setOnClickListener(this)
        binding.addTodo1ImageRoom.setOnClickListener(this)
        binding.addTodo1ImageKitchen.setOnClickListener(this)

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

    private fun setDialog() {
        val dialog = FairerDialog(requireContext())
        Timber.d("set dialog")
        dialog.showDialog()
        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                // ok btn click action
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.addTodo1ImageEntrance->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageEntrance.isSelected=true
                    Timber.d("click entrance")
                    binding.addTodo1Group.visibility=View.INVISIBLE
                    binding.addTodo1Group2.visibility=View.VISIBLE
                }
            }
            binding.addTodo1ImageLivingRoom->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageLivingRoom.isSelected=true
                    Timber.d("click livingroom")
                }
            }
            binding.addTodo1ImageBathroom->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageBathroom.isSelected=true
                    Timber.d("click bathroom")
                }
            }
            binding.addTodo1ImageOutside->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageOutside.isSelected=true
                    Timber.d("click outside")
                }
            }
            binding.addTodo1ImageRoom->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageRoom.isSelected=true
                    Timber.d("click room")
                }
            }
            binding.addTodo1ImageKitchen->{
                if(selected){
                    setDialog()
                }
                else{
                    selected=true
                    binding.addTodo1ImageKitchen.isSelected=true
                    Timber.d("click kitchen")
                }
            }
        }
    }

}