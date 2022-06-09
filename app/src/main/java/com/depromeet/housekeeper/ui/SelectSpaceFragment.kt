package com.depromeet.housekeeper.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.SelectSpaceChoreAdapter
import com.depromeet.housekeeper.databinding.FragmentSelectSpaceBinding
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.SpaceChores
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.ui.custom.dialog.DialogType
import com.depromeet.housekeeper.ui.custom.dialog.FairerDialog
import com.depromeet.housekeeper.util.VerticalItemDecorator
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*

class SelectSpaceFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSelectSpaceBinding
    private lateinit var myAdapter:SelectSpaceChoreAdapter
    private var selected: Boolean = false
    private val viewModel: SelectSpaceViewModel by viewModels()
    private val navArgs by navArgs<SelectSpaceFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_space, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.addCalendarView(navArgs.selectDate.date)
        binding.currentDate = viewModel.bindingDate()

        initListener()
        setAdapter()
        bindingVm()
        return binding.root
    }

    private fun initListener() {
        binding.selectSpaceImageEntrance.setOnClickListener(this)
        binding.selectSpaceImageLivingRoom.setOnClickListener(this)
        binding.selectSpaceImageBathroom.setOnClickListener(this)
        binding.selectSpaceImageOutside.setOnClickListener(this)
        binding.selectSpaceImageRoom.setOnClickListener(this)
        binding.selectSpaceImageKitchen.setOnClickListener(this)

        // go to 집안일 직접 추가 화면
        binding.selectSpaceGoDirectBtn.setOnClickListener {
            navigateToAddDirectTodoPage()
        }
        binding.selectSpaceGoDirectBtn2.setOnClickListener {
            navigateToAddDirectTodoPage()
        }

        // go to 다음 - 집안일 상세 화면
        binding.selectSpaceNextBtn.mainFooterButton.apply {
            text = resources.getString(R.string.next_btn_text)
            setOnClickListener {
                navigateToAddTodoPage2()
            }
        }

        // header 뒤로 가기
        binding.selectSpaceHeader.apply {
            defaultHeaderTitleTv.text = ""
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.selectSpaceCalender.setOnClickListener {
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
              viewModel.updateCalendarView(year, month, dayOfMonth)
          },
          calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH)-1,
          calendar.get(Calendar.DAY_OF_MONTH),
      )
      datePickerDialog.show()
  }


  private fun setAdapter(){
        val gridLayoutManager = GridLayoutManager(context,3)
        binding.selectSpaceRecyclerview.layoutManager=gridLayoutManager
        binding.selectSpaceRecyclerview.addItemDecoration(VerticalItemDecorator(10))
        myAdapter = SelectSpaceChoreAdapter(emptyList<String>())
        binding.selectSpaceRecyclerview.adapter = myAdapter
    }

    private fun bindingVm(){
        viewModel.clearChore()
        selected = false
        lifecycleScope.launchWhenStarted {
            viewModel.chorelist.collect {
                myAdapter = SelectSpaceChoreAdapter(viewModel.chorelist.value)
                myAdapter.notifyDataSetChanged()
                binding.selectSpaceRecyclerview.adapter = myAdapter

                myAdapter.setItemClickListener(object: SelectSpaceChoreAdapter.OnItemClickListener{
                    override fun onClick(v: View, chore:String, position: Int) {
                        v.isSelected = !v.isSelected
                        Timber.d("item click $position")
                        viewModel.updateChores(chore,v.isSelected)
                        binding.selectSpaceNextBtn.mainFooterButton.isEnabled = viewModel.getChoreCount() != 0
                        if(viewModel.getChoreCount()>0){
                            binding.selectSpaceGroup3.visibility=View.GONE
                        }
                        else{
                            binding.selectSpaceGroup3.visibility=View.VISIBLE
                        }
                    }
                })
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.selectCalendar.collect {
                binding.selectSpaceCalender.text = viewModel.bindingDate()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.isConnectedNetwork = it
            }
        }
    }

  private fun navigateToAddDirectTodoPage() {
    binding.selectSpaceGoDirectBtn.findNavController()
      .navigate(SelectSpaceFragmentDirections.actionSelectSpaceFragmentToAddDirectTodoFragment(
        viewType = ViewType.ADD,
        selectDate = viewModel.selectCalendar.value,
        houseWork = HouseWork(arrayListOf(), -1, "", "", null, "", false, null, 0)
      ))
  }

  private fun navigateToAddTodoPage2() {
    findNavController().navigate(SelectSpaceFragmentDirections.actionSelectSpaceFragmentToAddHouseWorkFramgent(
      SpaceChores(
        spaceName = viewModel.selectSpace.value,
        houseWorks = viewModel.chores.value,
      ), selectDate = viewModel.selectCalendar.value))
  }

    private fun setDialog(space : View?) {
        val dialog = FairerDialog(requireContext(), DialogType.CHANGE)
        Timber.d("set dialog")
        dialog.showDialog()
        dialog.onItemClickListener = object : FairerDialog.OnItemClickListener {
            override fun onItemClick() {
                selected = false
                binding.selectSpaceImageEntrance.isSelected = false
                binding.selectSpaceImageLivingRoom.isSelected = false
                binding.selectSpaceImageBathroom.isSelected = false
                binding.selectSpaceImageOutside.isSelected = false
                binding.selectSpaceImageRoom.isSelected = false
                binding.selectSpaceImageKitchen.isSelected = false
                binding.selectSpaceGroup.visibility=View.VISIBLE
                binding.selectSpaceGroup2.visibility=View.INVISIBLE
                binding.selectSpaceGroup3.visibility=View.INVISIBLE
                binding.selectSpaceGroup4.visibility=View.INVISIBLE
                viewEnabled()
                onClick(space)
            }
        }
    }

    override fun onClick(space: View?) {
        if (selected) {
            setDialog(space)
        }
        else {
            when (space) {
                binding.selectSpaceImageEntrance -> {
                    selected = true
                    binding.selectSpaceImageEntrance.isSelected = true
                    binding.selectSpaceImageEntrance.isEnabled = false
                    viewModel.setSpace("ENTRANCE")
                    viewModel.setChoreList("ENTRANCE")
                    viewChange()
                }
                binding.selectSpaceImageLivingRoom -> {
                    selected = true
                    binding.selectSpaceImageLivingRoom.isSelected = true
                    binding.selectSpaceImageLivingRoom.isEnabled = false
                    viewModel.setSpace("LIVINGROOM")
                    viewModel.setChoreList("LIVINGROOM")
                    viewChange()
                }
                binding.selectSpaceImageBathroom -> {
                    selected = true
                    binding.selectSpaceImageBathroom.isSelected = true
                    binding.selectSpaceImageBathroom.isEnabled = false
                    viewModel.setSpace("BATHROOM")
                    viewModel.setChoreList("BATHROOM")
                    viewChange()
                }
                binding.selectSpaceImageOutside -> {
                    selected = true
                    binding.selectSpaceImageOutside.isSelected = true
                    binding.selectSpaceImageOutside.isEnabled = false
                    viewModel.setSpace("OUTSIDE")
                    viewModel.setChoreList("OUTSIDE")
                    viewChange()
                }
                binding.selectSpaceImageRoom -> {
                    selected = true
                    binding.selectSpaceImageRoom.isSelected = true
                    binding.selectSpaceImageRoom.isEnabled = false
                    viewModel.setSpace("ROOM")
                    viewModel.setChoreList("ROOM")
                    viewChange()
                }
                binding.selectSpaceImageKitchen -> {
                    selected = true
                    binding.selectSpaceImageKitchen.isSelected = true
                    binding.selectSpaceImageKitchen.isEnabled = false
                    viewModel.setSpace("KITCHEN")
                    viewModel.setChoreList("KITCHEN")
                    viewChange()
                }
            }
        }
    }

    private fun viewChange(){
        binding.selectSpaceGroup.visibility=View.INVISIBLE
        binding.selectSpaceGroup2.visibility=View.VISIBLE
        binding.selectSpaceGroup3.visibility=View.VISIBLE
        binding.selectSpaceGroup4.visibility=View.VISIBLE
    }

    private  fun viewEnabled(){
        binding.selectSpaceImageEntrance.isEnabled = true
        binding.selectSpaceImageLivingRoom.isEnabled = true
        binding.selectSpaceImageBathroom.isEnabled = true
        binding.selectSpaceImageOutside.isEnabled = true
        binding.selectSpaceImageRoom.isEnabled = true
        binding.selectSpaceImageKitchen.isEnabled = true

    }

}