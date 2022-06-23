package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSettingProfileBinding
import kotlinx.coroutines.flow.collect

class SettingProfileFragment : Fragment() {
  lateinit var binding: FragmentSettingProfileBinding
  private val viewModel: SettingProfileViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_profile, container, false)
    binding.lifecycleOwner = this.viewLifecycleOwner
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initView()
    setListener()

    lifecycleScope.launchWhenCreated {
      viewModel.myData.collect {
        binding.etName.setText(it?.memberName)
        binding.etStatueMessage.setText(it?.statusMessage)
        binding.ivImageview.setImg(it?.profilePath)
      }
    }
  }

  private fun setListener() {
    binding.settingProfileHeader.apply {
      defaultHeaderBackBtn.setOnClickListener {
        it.findNavController().navigateUp()
      }
    }


    binding.profileBtn.mainFooterButton.setOnClickListener {
      viewModel.updateMe(
        binding.etName.text.toString(),
        binding.profilePath.toString(),
        binding.etStatueMessage.text.toString()
      )
      it.findNavController().navigateUp()
    }
  }

  private fun initView() {
    binding.profileBtn.mainFooterButton.text = "입력 완료"
    binding.profileBtn.mainFooterButton.isEnabled = true
  }

  private fun ImageView.setImg(url: String?) {
    Glide.with(binding.ivImageview.context)
      .load(url)
      .placeholder(R.drawable.bg_profile_imageview_inactive)
      .error(R.drawable.bg_profile_imageview_inactive)
      .diskCacheStrategy(DiskCacheStrategy.NONE)
      .fitCenter()
      .override(100, 100)
      .into(binding.ivImageview)
  }


}