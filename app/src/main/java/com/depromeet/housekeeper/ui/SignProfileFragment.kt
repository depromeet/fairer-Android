package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.adapter.SignProfileAdapter
import com.depromeet.housekeeper.databinding.FragmentSignProfileBinding
import com.depromeet.housekeeper.util.VerticalItemDecorator
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SignProfileFragment : Fragment() {
    lateinit var binding: FragmentSignProfileBinding
    private val viewModel: SignProfileViewModel by viewModels()
    private lateinit var myAdapter: SignProfileAdapter
    private val navArgs by navArgs<SignProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_profile, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(navArgs.name)
        setAdapter()
        bindingVm()
        initListener()
    }

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
    }

    private fun initListener() {
        binding.signProfileHeader.defaultHeaderTitleTv.text = ""
        binding.signNameNextBtn.mainFooterButton.setText(R.string.sign_profile_next_btn_text)
        binding.signProfileHeader.defaultHeaderBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signNameNextBtn.mainFooterButton.setOnClickListener {
            findNavController().navigate(
                SignProfileFragmentDirections.actionSignProfileFragmentToJoinGroupFragment(
                )
            )
        }
    }

    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(context, 4)
        val dummyList: List<ProfileState> = listOf(
            ProfileState("https://i.pinimg.com/originals/61/0b/12/610b12fdc6afe3beafd439b43a52ad24.png",false),
            ProfileState("https://www.urbanbrush.net/web/wp-content/uploads/edd/2020/11/urbanbrush-20201104103659627968.jpg", false)
        )
        binding.signProfileRecyclerImageview.layoutManager = gridLayoutManager
        myAdapter = SignProfileAdapter(dummyList)
        binding.signProfileRecyclerImageview.adapter = myAdapter
        binding.signProfileRecyclerImageview.addItemDecoration(VerticalItemDecorator(16))
        myAdapter.setItemClickListener(object : SignProfileAdapter.OnItemClickListener {
            override fun onClick(v: View, imgUrl: String, position: Int) {
                viewModel.setSelectedImage(imgUrl)
            }
        })
    }

    data class ProfileState(
        val url : String,
        var state : Boolean,
    )
}