package com.depromeet.housekeeper.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentMainBinding
import com.depromeet.housekeeper.databinding.PopupFeedbackMenuBinding
import com.depromeet.housekeeper.databinding.PopupFeedbackMenuHasFeedbackBinding
import com.depromeet.housekeeper.model.AssigneeSelect
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.FeedbackHouseworkResponse
import com.depromeet.housekeeper.model.enums.HouseWorkState
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.response.FeedbackFindOneResponseDto
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.model.response.HouseWorks
import com.depromeet.housekeeper.ui.main.adapter.DayOfWeekAdapter
import com.depromeet.housekeeper.ui.main.adapter.FeedbackAdapter
import com.depromeet.housekeeper.ui.main.adapter.GroupProfileAdapter
import com.depromeet.housekeeper.ui.main.adapter.HouseWorkAdapter
import com.depromeet.housekeeper.ui.main.dialog.ReturnFeedbackDialog
import com.depromeet.housekeeper.ui.main.dialog.UrgeBottomDialog
import com.depromeet.housekeeper.util.*
import com.depromeet.housekeeper.util.EditTextUtil.listenEditorDoneAction
import com.depromeet.housekeeper.util.NavigationUtil.navigateSafe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private lateinit var dayOfAdapter: DayOfWeekAdapter
    private var houseWorkAdapter: HouseWorkAdapter? = null
    private lateinit var groupProfileAdapter: GroupProfileAdapter
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var popupWindow: PopupWindow
    private lateinit var feedbackAdapter: FeedbackAdapter
    private val Int.dpToPx: Int
        get() = (this * resources.displayMetrics.density).toInt()


    override fun createView(binding: FragmentMainBinding) {
        binding.vm = mainViewModel
        Timber.d("createView")
    }

    override fun viewCreated() {
        setAdapter()
        mainViewModel.apply {
            getRules()
            getGroupName()
            if (this.dayOfWeek.value == DayOfWeek(date = "")) {
                Timber.d("asd date today")
                updateSelectDate(DateUtil.getTodayFull())
                updateStartDateOfWeek(DateUtil.getCurrentStartDate())
            } else {
                dayOfAdapter.updateDate(getSelectWeek())
                updateSelectDate(dayOfWeek.value)
            }
        }
        initView()
        bindingVm()
        setListener()
    }


    private fun initView() {
        val userNameFormat =
            String.format(resources.getString(R.string.user_name), PrefsManager.userName)
        binding.tvName.text = getSpannableText(userNameFormat, 0, userNameFormat.indexOf("님"))
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    private fun setListener() {
        binding.btAddTodo.addChoreView.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSelectSpaceFragment(
                    mainViewModel.dayOfWeek.value
                )
            )
        }
        binding.tvMonth.setOnClickListener {
            createDatePickerDialog()
        }

        binding.mainHeader.mainHeaderSettingIv.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingFragment())
        }

        binding.lvRule.root.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRuleFragment())
        }
        binding.tvToday.setOnClickListener {
            dayOfAdapter.updateDate(DateUtil.getCurrentWeek())
            mainViewModel.updateSelectDate(DateUtil.getTodayFull())
            mainViewModel.updateStartDateOfWeek(DateUtil.getCurrentStartDate())
        }

        binding.btnStatistics.setOnClickListener {
            findNavController().navigateSafe(R.id.action_mainFragment_to_statisticsFragment)
        }
    }

    private fun setAdapter() {
        feedbackAdapter = FeedbackAdapter(emptyList<FeedbackFindOneResponseDto>().toMutableList())
        dayOfAdapter = DayOfWeekAdapter(DateUtil.getCurrentWeek(),
            onClick = {
                mainViewModel.updateSelectDate(it)
            })
        val animator: RecyclerView.ItemAnimator? = binding.rvWeek.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        val gridLayoutManager = GridLayoutManager(context, 7)
        binding.rvWeek.layoutManager = gridLayoutManager
        binding.rvWeek.adapter = dayOfAdapter
        rvWeekSwipeListener()

        val list =
            mainViewModel.selectHouseWorks.value?.houseWorks?.toMutableList() ?: mutableListOf()
        houseWorkAdapter = HouseWorkAdapter(list,
            onClick = { mainViewModel.getDetailHouseWork(it.houseWorkId) },
            onDone = { it, isEmojiEmpty ->
                if (it.houseWorkCompleteId == 0) {
                    mainViewModel.updateChoreState(it)
                } else {
                    if (isEmojiEmpty) {
                        mainViewModel.cancelChoreComplete(it)
                    } else {
                        ReturnFeedbackDialog(
                            onReturnClick = { mainViewModel.cancelChoreComplete(it) }
                        ).show(requireActivity().supportFragmentManager, "tag")
                    }

                }
                mainViewModel.getCompleteHouseWorkNumber()
            },
            onLongClick = { view, success, isTimeOver, houseWork ->
                if (success) {
                    if (houseWork.feedbackHouseworkResponse?.get("0")?.myFeedback != true) {
                        setFeedbackPopupMenu(houseWork, houseWork.houseWorkCompleteId)
                        popupWindow.showAsDropDown(view, 0, (-204).dpToPx)
                    } else {
                        setFeedbackPopupMenu(houseWork, houseWork.houseWorkCompleteId)
                        popupWindow.showAsDropDown(view, 0, (-290).dpToPx)
                    }
                } else {
                    if (isTimeOver) {
                        UrgeBottomDialog(
                            onUrgeClick = {mainViewModel.urgeHousework(houseWork.houseWorkId,houseWork.scheduledDate)},
                        ).show(requireActivity().supportFragmentManager, "tag")
                    }
                }
            },
            feedbackClick = { houseWorkCompleteId ->
                if (houseWorkCompleteId != null) {
                    lifecycleScope.launch {
                        mainViewModel.getFeedbackList(houseWorkCompleteId = houseWorkCompleteId)
                        mainViewModel.feedbackList.collectLatest { feedbackList ->
                            if (feedbackList != null) {
                                feedbackAdapter.updateFeedbackList(feedbackList.feedbackFindOneResponseDtoList)
                            }
                        }
                    }
                    showFeedbackBottomSheet()
                }
            }
        )
        binding.rvHouseWork.adapter = houseWorkAdapter
        binding.rvHouseWork.addItemDecoration(VerticalItemDecorator(20))
        val swipeHelperCallback = SwipeHelperCallback(houseWorkAdapter!!).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 5)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvHouseWork)
        binding.rvHouseWork.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.rvHouseWork)
            false
        }
        groupProfileAdapter = GroupProfileAdapter(mainViewModel.groups.value.toMutableList()) {
            mainViewModel.updateSelectUser(it.memberId)
        }
        binding.rvGroups.adapter = groupProfileAdapter
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.completeChoreNum.collect {
                when (it) {
                    0 -> {
                        binding.tvCompleteHouseChore.text = getString(R.string.complete_chore_yet)
                    }

                    else -> {
                        val completeFormat =
                            String.format(resources.getString(R.string.complete_chore), it)
                        binding.tvCompleteHouseChore.text =
                            getSpannableText(
                                completeFormat,
                                completeFormat.indexOf("에") + 1,
                                completeFormat.indexOf("나")
                            )
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.weekendHouseWorks.collect {
                Timber.d("$MAIN_TAG : weekendHouseWorks : ${it.keys}")
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.weekendChoresLeft.collect {
                Timber.d("$MAIN_TAG : weekendChoresLeft : ${it}")
                dayOfAdapter.updateLeftCnt(mainViewModel.weekendChoresLeft.value)
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.selectHouseWorks.collectLatest {
                Timber.d("$MAIN_TAG collect \n$it")
                if (it != null) {
                    if (it.countLeft == 0) {
                        if (it.countDone > 0) {
                            binding.houseworkState = HouseWorkState.DONE
                        } else if (it.countDone == 0) {
                            binding.houseworkState = HouseWorkState.EMPTY
                        }
                    } else if (it.countLeft > 0) {
                        binding.houseworkState = HouseWorkState.LEFT
                    }

                    updateHouseWorkData(it)
                } else {
                    binding.houseworkState = HouseWorkState.LEFT
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.selectedHouseWorkItem.collect {
                if (it != null) {
                    mainViewModel.setSelectedHouseWorkItem(null)
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAddDirectTodoFragment(
                            viewType = ViewType.EDIT,
                            houseWork = it,
                            selectDate = mainViewModel.dayOfWeek.value
                        )
                    )
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.dayOfWeek.collect {
                Timber.d("$MAIN_TAG : selectedDate $it")
                mainViewModel.updateSelectHouseWork(it.date.substring(0, 10))
                val year = it.date.split("-")[0]
                val month = it.date.split("-")[1]
                binding.tvMonth.text = "${year}년 ${month}월"
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.startDateOfWeek.collect {
                Timber.d("$MAIN_TAG : startDateOfWeek $it")
                mainViewModel.getHouseWorks()
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.groupName.collect {
                binding.tvGroupName.text = it
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.groups.collect {
                val profileGroups: List<AssigneeSelect> = it.map {
                    AssigneeSelect(
                        it.memberId,
                        it.memberName,
                        it.profilePath,
                        it.memberId == mainViewModel.selectUserId.value
                    )
                }
                groupProfileAdapter.updateDate(profileGroups.toMutableList())
            }
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.rule.collect {
                binding.lvRule.rule = it
            }
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.selectUserId.collect {
                mainViewModel.getHouseWorks()
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
                if (it) {
                    val fm = requireActivity().supportFragmentManager
                    for (i in 0..fm.backStackEntryCount) {
                        fm.popBackStack()
                        Timber.d("back stack $i")
                    }
                }
            }
        }
    }

    private fun getSpannableText(format: String, firstIndex: Int, lastIndex: Int): SpannableString {
        val spannerString2 = SpannableString(format).apply {
            setSpan(
                ForegroundColorSpan(Color.parseColor("#0C6DFF")),
                firstIndex,
                lastIndex,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannerString2
    }

    private fun updateHouseWorkData(houseWork: HouseWorks) {
        val remainList =
            houseWork.houseWorks
                .filter { !it.success }
                .sortedBy { it.scheduledTime }
                .toMutableList()

        val doneList =
            houseWork.houseWorks
                .filter { it.success }
                .sortedBy { it.scheduledTime }
                .toMutableList()

        houseWorkAdapter?.updateDate(remainList, doneList)
    }


    private fun rvWeekSwipeListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ACTION_STATE_SWIPE) {
                    val view = binding.rvWeek
                    val newX = 0.0 // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        view,
                        newX.toFloat(),
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        dayOfAdapter.updateDate(mainViewModel.getNextWeek())

                    }

                    ItemTouchHelper.RIGHT -> dayOfAdapter.updateDate(mainViewModel.getLastWeek())
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvWeek)
    }

    private fun createDatePickerDialog() {
        val currentDate = mainViewModel.dayOfWeek.value
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->
                //TODO("DayOfWeek Adapter 변경")
                val list = mainViewModel.getDatePickerWeek(year, month, dayOfMonth)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectDate = DateUtil.fullDateFormat.format(calendar.time)
                dayOfAdapter.updateDate(list)
                mainViewModel.updateSelectDate(DayOfWeek(selectDate, true))
                mainViewModel.updateStartDateOfWeek(DateUtil.getStartDate(selectDate))
            },
            currentDate.date.split("-")[0].toInt(),
            currentDate.date.split("-")[1].toInt() - 1,
            currentDate.date.split("-")[2].toInt(),
        )
        datePickerDialog.show()
    }

    private fun setFeedbackPopupMenu(houseWork: HouseWork, houseWorkCompleteId: Int?) {
        val binding =
            if (houseWork.feedbackHouseworkResponse?.get("0")?.myFeedback == false
            ) PopupFeedbackMenuBinding.inflate(layoutInflater) else PopupFeedbackMenuHasFeedbackBinding.inflate(
                layoutInflater
            )
        if (findTrueMyFeedback(houseWork.feedbackHouseworkResponse!!) == null) {
            (binding as PopupFeedbackMenuBinding).selectedNum = -1
        } else if (findTrueMyFeedback(houseWork.feedbackHouseworkResponse) != 0) {
            (binding as PopupFeedbackMenuBinding).selectedNum = findTrueMyFeedback(houseWork.feedbackHouseworkResponse)!!
        }


        val popupView = binding.root
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            272f,
            requireContext().resources.displayMetrics
        )
        popupWindow.width = width.toInt()
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(requireContext().getDrawable(R.drawable.popup_background))
        popupWindow.elevation = 10.0F
        if (houseWork.feedbackHouseworkResponse["0"]?.myFeedback == false) {
            (binding as PopupFeedbackMenuBinding).apply {
                clDialogFeedbackUrgeTop.setOnClickListener {
                showEditTextBottomSheet(false,houseWorkCompleteId)
            }
                listOf(icAngry, icSad, icSmile, icSuperSmile, icHeart, ic100).forEachIndexed {index, view ->
                    view.setOnClickListener {
                        // 각 뷰에 대한 클릭 리스너에서 수행할 작업 구현
                        mainViewModel.createFeedback(null,index+1,houseWorkCompleteId!!)
                        popupWindow.dismiss()
                    }
                }
            }
        } else {
            (binding as PopupFeedbackMenuHasFeedbackBinding).apply {
                clPopupFeedbackModify.setOnClickListener {
                    showEditTextBottomSheet(true,houseWorkCompleteId)
                }

                clDialogFeedbackDelete.setOnClickListener {
                    mainViewModel.deleteFeedback(houseWork.feedbackHouseworkResponse["0"]!!.feedbackId)
                    popupWindow.dismiss()
                }
                listOf(icAngry, icSad, icSmile, icSuperSmile, icHeart, ic100).forEachIndexed {index, view ->
                    view.setOnClickListener {
                        // 각 뷰에 대한 클릭 리스너에서 수행할 작업 구현
                        mainViewModel.createFeedback(null,index+1,houseWorkCompleteId!!)
                        popupWindow.dismiss()
                    }
                }
            }
        }
    }

    // 화면에서 바텀 시트를 띄우기 위해 사용하는 함수
    private fun showEditTextBottomSheet(hasTextFeedback : Boolean,houseWorkCompleteId: Int?) {
        val textBottomSheet = BottomSheetDialog(requireContext())
        var bottomSheetBehavior: BottomSheetBehavior<View>
        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_edit_text, null)
        textBottomSheet.setContentView(bottomSheetView)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val layout = textBottomSheet.findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        val etFeedback = textBottomSheet.findViewById<EditText>(R.id.et_feedback)
        layout!!.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = 180
        textBottomSheet.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 상태 변화 감지
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // BottomSheet가 축소되어 있을 때 필요한 작업 수행
                    bottomSheetBehavior.peekHeight = 180 // BottomSheet 원래 크기로 설정
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // BottomSheet가 확장되어 있을 때 필요한 작업 수행
                    bottomSheetBehavior.peekHeight =
                        180 // BottomSheet가 확장되어 있을 때는 peek height를 0으로 설정
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // BottomSheet가 슬라이드되는 동안 실행되는 작업 수행
            }
        })

        etFeedback?.apply {
            listenEditorDoneAction {
                if (it.length <= 16) {
                    EditTextUtil.hideKeyboard(requireContext(), this)
                    if (houseWorkCompleteId != null) {
                        if(hasTextFeedback){
                            mainViewModel.updateFeedback(houseWorkCompleteId,it)
                        }
                        else{
                            mainViewModel.createFeedback(it, 0, houseWorkCompleteId)
                        }
                    }
                    textBottomSheet.dismiss()
                    popupWindow.dismiss()
                }
            }
            textBottomSheet.show()
        }
    }

    @SuppressLint("InflateParams")
    private fun showFeedbackBottomSheet() {
        val feedbackBottomSheet = BottomSheetDialog(requireContext())
        var bottomSheetBehavior: BottomSheetBehavior<View>
        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_feedback, null)
        val background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottom_sheet)
        bottomSheetView.background = background
        feedbackBottomSheet.setContentView(bottomSheetView)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val layout = feedbackBottomSheet.findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        val feedbackRv = feedbackBottomSheet.findViewById<RecyclerView>(R.id.rv_feedback_item)
        feedbackRv?.adapter = feedbackAdapter

        layout!!.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = 210.dpToPx
        feedbackBottomSheet.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 상태 변화 감지
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // BottomSheet가 축소되어 있을 때 필요한 작업 수행
                    bottomSheetBehavior.peekHeight = 210.dpToPx // BottomSheet 원래 크기로 설정
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // BottomSheet가 확장되어 있을 때 필요한 작업 수행
                    bottomSheetBehavior.peekHeight =
                        210.dpToPx // BottomSheet가 확장되어 있을 때는 peek height를 210dp으로 설정
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // BottomSheet가 슬라이드되는 동안 실행되는 작업 수행
            }
        })
        feedbackBottomSheet.show()
    }
    private fun findTrueMyFeedback(map: Map<String, FeedbackHouseworkResponse>): Int? {
        for (i in 0..6) {
            val key = i.toString()
            val response = map[key]
            if (response != null && response.myFeedback) {
                return i
            }
        }
        return null
    }



}