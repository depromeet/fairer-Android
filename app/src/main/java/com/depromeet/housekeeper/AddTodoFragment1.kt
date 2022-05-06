package com.depromeet.housekeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.adapter.AddTodo1ChoreAdapter
import com.depromeet.housekeeper.databinding.FragmentAddTodo1Binding
import com.depromeet.housekeeper.model.SpaceChores
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class AddTodoFragment1 : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAddTodo1Binding
    private lateinit var myAdapter:AddTodo1ChoreAdapter
    private var selected: Boolean = false
    private val viewModel: AddTodoFragment1ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo1, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner


        initListener()
        setAdapter()
        bindingVm()
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
        binding.addTodo1GoDirectBtn2.setOnClickListener {
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

    private fun setAdapter(){
        val gridLayoutManager = GridLayoutManager(context,3)
        binding.addTodo1Recyclerview.layoutManager=gridLayoutManager
        myAdapter = AddTodo1ChoreAdapter(emptyList<String>())
        binding.addTodo1Recyclerview.adapter = myAdapter
    }

    private fun bindingVm(){
        lifecycleScope.launchWhenStarted {
            viewModel.chorelist.collect {
                myAdapter = AddTodo1ChoreAdapter(viewModel.chorelist.value)
                myAdapter.notifyDataSetChanged()
                binding.addTodo1Recyclerview.adapter = myAdapter

                myAdapter.setItemClickListener(object: AddTodo1ChoreAdapter.OnItemClickListener{
                    override fun onClick(v: View, chore:String, position: Int) {
                        v.isSelected = !v.isSelected
                        Timber.d("item click $position")
                        viewModel.updateChores(chore,v.isSelected)
                        binding.addTodo1NextBtn.mainFooterButton.isEnabled = viewModel.getChoreCount() != 0
                        if(viewModel.getChoreCount()>0){
                            binding.addTodo1Group3.visibility=View.GONE
                        }
                        else{
                            binding.addTodo1Group3.visibility=View.VISIBLE
                        }
                    }
                })
            }
        }
    }

    private fun navigateToAddDirectTodoPage() {
        binding.addTodo1GoDirectBtn.findNavController()
            .navigate(R.id.action_addTodoFragment1_to_addDirectTodoFragment)
    }

    private fun navigateToAddTodoPage2() {
        findNavController().navigate(AddTodoFragment1Directions.actionAddTodoFragment1ToAddTodoFragment2(
            SpaceChores(spaceName = viewModel.selectSpace.value,houseWorks = viewModel.chores.value)))
    }

    private fun setDialog() {
        val dialog = FairerDialog(requireContext())
        Timber.d("set dialog")
        dialog.showDialog()
        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                selected = false
                binding.addTodo1ImageEntrance.isSelected = false
                binding.addTodo1ImageLivingRoom.isSelected = false
                binding.addTodo1ImageBathroom.isSelected = false
                binding.addTodo1ImageOutside.isSelected = false
                binding.addTodo1ImageRoom.isSelected = false
                binding.addTodo1ImageKitchen.isSelected = false
                binding.addTodo1Group.visibility=View.VISIBLE
                binding.addTodo1Group2.visibility=View.INVISIBLE
                binding.addTodo1Group3.visibility=View.INVISIBLE
                binding.addTodo1Group4.visibility=View.INVISIBLE
                viewenabled()
            }
        }
    }

    override fun onClick(p0: View?) {
        if (selected) {
            setDialog()
        }
        else {
            when (p0) {
                binding.addTodo1ImageEntrance -> {
                    selected = true
                    binding.addTodo1ImageEntrance.isSelected = true
                    binding.addTodo1ImageEntrance.isEnabled = false
                    viewModel.setSpace("entrance")
                    viewModel.setChoreList("entrance")
                    viewChange()
                }
                binding.addTodo1ImageLivingRoom -> {
                    selected = true
                    binding.addTodo1ImageLivingRoom.isSelected = true
                    binding.addTodo1ImageLivingRoom.isEnabled = false
                    viewModel.setSpace("livingroom")
                    viewModel.setChoreList("livingroom")
                    viewChange()
                }
                binding.addTodo1ImageBathroom -> {
                    selected = true
                    binding.addTodo1ImageBathroom.isSelected = true
                    binding.addTodo1ImageBathroom.isEnabled = false
                    viewModel.setSpace("bathroom")
                    viewModel.setChoreList("bathroom")
                    viewChange()
                }
                binding.addTodo1ImageOutside -> {
                    selected = true
                    binding.addTodo1ImageOutside.isSelected = true
                    binding.addTodo1ImageOutside.isEnabled = false
                    viewModel.setSpace("outside")
                    viewModel.setChoreList("outside")
                    viewChange()
                }
                binding.addTodo1ImageRoom -> {
                    selected = true
                    binding.addTodo1ImageRoom.isSelected = true
                    binding.addTodo1ImageRoom.isEnabled = false
                    viewModel.setSpace("room")
                    viewModel.setChoreList("room")
                    viewChange()
                }
                binding.addTodo1ImageKitchen -> {
                    selected = true
                    binding.addTodo1ImageKitchen.isSelected = true
                    binding.addTodo1ImageKitchen.isEnabled = false
                    viewModel.setSpace("kitchen")
                    viewModel.setChoreList("kitchen")
                    viewChange()
                }
            }
        }
    }

    private fun viewChange(){
        binding.addTodo1Group.visibility=View.INVISIBLE
        binding.addTodo1Group2.visibility=View.VISIBLE
        binding.addTodo1Group3.visibility=View.VISIBLE
        binding.addTodo1Group4.visibility=View.VISIBLE
    }

    private  fun viewenabled(){
        binding.addTodo1ImageEntrance.isEnabled = true
        binding.addTodo1ImageLivingRoom.isEnabled = true
        binding.addTodo1ImageBathroom.isEnabled = true
        binding.addTodo1ImageOutside.isEnabled = true
        binding.addTodo1ImageRoom.isEnabled = true
        binding.addTodo1ImageKitchen.isEnabled = true

    }

}