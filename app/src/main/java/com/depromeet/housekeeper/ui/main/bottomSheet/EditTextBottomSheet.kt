package com.depromeet.housekeeper.ui.main.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.housekeeper.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditTextBottomSheet(): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_edit_text, container, false)

        val bottomSheet = view.findViewById<View>(R.id.bottom_sheet_container)
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.state = BottomSheetBehavior.STATE_EXPANDED // 최초에는 바텀시트를 축소된 상태로 시작

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 바텀시트가 축소된 상태일 때 실행할 코드
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 바텀시트가 전체 확장된 상태일 때 실행할 코드
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss() // 바텀시트 종료
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 중일 때 실행할 코드
            }
        })
        return view
    }
}