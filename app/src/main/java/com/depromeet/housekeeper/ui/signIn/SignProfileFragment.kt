package com.depromeet.housekeeper.ui.signIn

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentSignProfileBinding
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.ui.signIn.adapter.SignProfileAdapter
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.util.VerticalItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignProfileFragment : BaseFragment<FragmentSignProfileBinding>(R.layout.fragment_sign_profile) {
    private val viewModel: SignProfileViewModel by viewModels()
    private lateinit var myAdapter: SignProfileAdapter
    private val navArgs by navArgs<SignProfileFragmentArgs>()

    override fun createView(binding: FragmentSignProfileBinding) {
        binding.vm = viewModel
    }

    override fun viewCreated() {
        Timber.d(navArgs.name)
        initView()
        initListener()
        setAdapter()
        bindingVm()
    }

    private fun initView() {
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindingVm() {
        viewModel.setViewType(navArgs.viewType)
        binding.viewType = viewModel.viewType.value
        lifecycleScope.launchWhenCreated {
            viewModel.isSelected.collect {
                if (it) {
                    binding.signNameNextBtn.mainFooterButton.isEnabled = true
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.profileImageList.collect {
                setAdapter()
                myAdapter.notifyDataSetChanged()
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.updateMemberResponse.collect {
                it?.run {
                    Timber.d("updateMemberResponse : $it")
                    PrefsManager.setUserName(viewModel.memberName.value)
                    findNavController().navigate(
                        SignProfileFragmentDirections.actionSignProfileFragmentToJoinGroupFragment()
                    )
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
            }
        }
    }

    private fun initListener() {
        navArgs.name?.let { viewModel.setMemberName(it) }

        binding.signProfileHeader.defaultHeaderTitleTv.text = ""

        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_profile_next_btn_text)

        binding.signProfileHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            if (viewModel.viewType.value == ProfileViewType.Sign) {
                viewModel.requestUpdateMember()
            }
            if (viewModel.viewType.value == ProfileViewType.Modify) {
                it.findNavController().navigate(
                    SignProfileFragmentDirections.actionSignProfileFragmentToSettingProfileFragment(
                        viewModel.selectedImage.value
                    )
                )
            }
        }
    }

    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 4)
        binding.signProfileRecyclerImageview.layoutManager = gridLayoutManager
        myAdapter = SignProfileAdapter(viewModel.profileImageList.value, requireContext())
        binding.signProfileRecyclerImageview.adapter = myAdapter
        binding.signProfileRecyclerImageview.addItemDecoration(VerticalItemDecorator(16))
        myAdapter.setItemClickListener(object : SignProfileAdapter.OnItemClickListener {
            override fun onClick(v: View, imgUrl: String, position: Int) {
                viewModel.setSelectedImage(imgUrl)
            }
        })
    }

}