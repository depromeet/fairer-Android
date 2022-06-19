package com.depromeet.housekeeper.ui.custom.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.housekeeper.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AssigneeBottomSheetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignee_bottom_sheet_dialog, container, false)
    }

}