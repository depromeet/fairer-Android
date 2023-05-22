package com.depromeet.housekeeper.ui.roulette

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bluehomestudio.luckywheel.LuckyWheel
import com.bluehomestudio.luckywheel.WheelItem
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentRouletteBinding
import com.depromeet.housekeeper.ui.addHousework.selectTime.adapter.AddAssigneeAdapter
import com.depromeet.housekeeper.ui.custom.dialog.AssigneeBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouletteFragment : BaseFragment<FragmentRouletteBinding>(R.layout.fragment_roulette) {
    private val viewModel: RouletteViewModel by viewModels()
    lateinit var addAssigneeAdapter: AddAssigneeAdapter
    private lateinit var luckyWheel: LuckyWheel
    private var wheelItems : List<WheelItem> = emptyList()
    override fun createView(binding: FragmentRouletteBinding) {
        luckyWheel = binding.lwv
        luckyWheel.addWheelItems(wheelItems)
    }

    override fun viewCreated() {
        initView()
        setAdapter()
        bindingVm()
        initListener()
    }

    private fun bindingVm(){
        lifecycleScope.launchWhenCreated {
            viewModel.curAssignees.collect {
                addAssigneeAdapter.updateAssignees(it)
            }
        }
    }

    private fun initView() {
        binding.ctaBtn.mainFooterButton.text = getString(R.string.roulette_cta_button)
    }

    private fun setAdapter() {
        // 집안일 담당자 adapter
        addAssigneeAdapter = AddAssigneeAdapter(viewModel.curAssignees.value)
        binding.addAssigneeRv.adapter = addAssigneeAdapter
    }

    private fun initListener() {
        binding.addAssigneeBtn.setOnClickListener {
            createBottomSheet()
        }
    }
    /**
     * Dialog
     */

    private fun createBottomSheet() {
        val bottomSheet = AssigneeBottomSheetDialog(
            allGroup = viewModel.allGroupInfo.value,
            curAssignees = viewModel.curAssignees.value,
            onClick = null
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
        bottomSheet.setMyOkBtnClickListener(object :
            AssigneeBottomSheetDialog.MyOkBtnClickListener {
            override fun onOkBtnClick() {
                viewModel.setCurAssignees(bottomSheet.selectedAssignees)
                addAssigneeAdapter.updateAssignees(viewModel.curAssignees.value)
            }
        })
    }
}