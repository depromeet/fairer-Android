package com.depromeet.housekeeper.ui.custom.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentAssigneeBottomSheetDialogBinding
import com.depromeet.housekeeper.model.Assignee
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AssigneeBottomSheetDialog(
    val allGroup: ArrayList<Assignee>,
    private val curAssignees: ArrayList<Assignee>,
    private val onClick: (() -> Unit)?
    ) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAssigneeBottomSheetDialogBinding
    lateinit var bottomSheetAssigneeAdapter: BottomSheetAssigneeAdapter
    private lateinit var mOkBtnClickListener: MyOkBtnClickListener

    interface MyOkBtnClickListener {
        fun onOkBtnClick()
    }

    fun setMyOkBtnClickListener(okBtnClickListener: MyOkBtnClickListener) {
        mOkBtnClickListener = okBtnClickListener
    }

    val selectedAssignees: ArrayList<Assignee> = curAssignees

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_assignee_bottom_sheet_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initClickListener()
    }

    private fun setAdapter() {
        bottomSheetAssigneeAdapter = BottomSheetAssigneeAdapter(allGroup, curAssignees)
        binding.bottomSheetAssigneeRv.adapter = bottomSheetAssigneeAdapter
        if(onClick==null){
            binding.clRoulette.visibility = View.GONE
        }
    }

    private fun initClickListener() {
        binding.bottomSheetDlgCancelBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.bottomSheetDlgOkBtn.setOnClickListener {
            val curSelectedAssignees = bottomSheetAssigneeAdapter.getSelectedAssignees()
            when {
                curSelectedAssignees.isNotEmpty() -> {
                    addAssignee()
                    mOkBtnClickListener.onOkBtnClick()
                    dialog!!.dismiss()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.add_assignee_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.clRoulette.setOnClickListener {
            onClick?.invoke()
        }
    }

    private fun addAssignee() {
        selectedAssignees.clear()
        selectedAssignees.addAll(bottomSheetAssigneeAdapter.getSelectedAssignees())
    }

}