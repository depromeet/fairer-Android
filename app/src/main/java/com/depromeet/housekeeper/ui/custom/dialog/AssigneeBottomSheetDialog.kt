package com.depromeet.housekeeper.ui.custom.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.BottomSheetAssigneeAdapter
import com.depromeet.housekeeper.databinding.FragmentAssigneeBottomSheetDialogBinding
import com.depromeet.housekeeper.model.Assignee
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AssigneeBottomSheetDialog(val allGroup: ArrayList<Assignee>, private val myInfo: Assignee) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAssigneeBottomSheetDialogBinding
    lateinit var bottomSheetAssigneeAdapter: BottomSheetAssigneeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assignee_bottom_sheet_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initClickListener()
    }

    private fun setAdapter() {
        bottomSheetAssigneeAdapter = BottomSheetAssigneeAdapter(allGroup)
        binding.bottomSheetAssigneeRv.adapter = bottomSheetAssigneeAdapter
    }

    private fun initClickListener() {
        binding.bottomSheetDlgCancelBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.bottomSheetDlgOkBtn.setOnClickListener {
            // TODO : 선택 된 assignees 반환
            dialog!!.dismiss()
        }
    }

}