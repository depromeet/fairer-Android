package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentSettingProfileBinding
import com.depromeet.housekeeper.model.enums.ProfileViewType

class SettingProfileFragment : Fragment() {
    lateinit var binding: FragmentSettingProfileBinding
    private val viewModel: SettingProfileViewModel by viewModels()
    private val navArgs by navArgs<SettingProfileFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setting_profile, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        initView()
        setListener()
        validateName()

        lifecycleScope.launchWhenCreated {
            viewModel.myData.collect {
                binding.etName.fairerEt.setText(it?.memberName)
                binding.etStatusMessage.fairerEt.setText(it?.statusMessage)
                when {
                    !navArgs.profilePath.isNullOrEmpty() -> {
                        viewModel.updateProfile(navArgs.profilePath!!)
                        binding.ivImageview.setImg(navArgs.profilePath!!)
                    }
                    else -> {
                        binding.ivImageview.setImg(it?.profilePath)
                    }
                }
                binding.nameIsTextChanged = false
                binding.stateIsTextChanged = false
            }
        }

    }

    private fun validateName() {
        val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"
        binding.etName.fairerEt.addTextChangedListener {
            val value: String = binding.etName.fairerEt.text.toString()
            if (!value.matches(pattern.toRegex())) {
                binding.nameIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
            } else {
                binding.nameIsError = false
                binding.profileBtn.mainFooterButton.isEnabled =
                    value.isNotEmpty()
            }
            binding.nameIsTextChanged = value != ""
        }
        binding.etStatusMessage.fairerEt.addTextChangedListener {
            val value: String = binding.etStatusMessage.fairerEt.text.toString()
            if (!value.matches(pattern.toRegex())) {
                binding.stateIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
            } else {
                binding.stateIsError = false
                binding.profileBtn.mainFooterButton.isEnabled = true
            }
            binding.nameIsTextChanged = value != ""
        }
    }

    private fun setListener() {
        binding.settingProfileBackground.setOnClickListener {
            binding.etName.isTextChanged = false
            binding.etStatusMessage.isTextChanged = false
            binding.etName.fairerEt.isEnabled = false
            binding.etStatusMessage.fairerEt.isEnabled = false
            binding.etName.fairerEt.isEnabled = true
            binding.etStatusMessage.fairerEt.isEnabled = true
        }
        binding.etName.signNameClear.setOnClickListener {
            binding.etName.fairerEt.setText(R.string.sign_name_blank)
        }
        binding.etStatusMessage.signNameClear.setOnClickListener {
            binding.etStatusMessage.fairerEt.setText(R.string.sign_name_blank)
        }

        binding.lvProfileImageview.setOnClickListener {
            it.findNavController()
                .navigate(
                    SettingProfileFragmentDirections.actionSettingProfileFragmentToSignProfileFragment(
                        ProfileViewType.Modify, ""
                    )
                )
        }

        binding.settingProfileHeader.apply {
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.profileBtn.mainFooterButton.setOnClickListener {
            if (navArgs.profilePath != null) {
                viewModel.updateMe(
                    binding.etName.fairerEt.text.toString(),
                    navArgs.profilePath!!,
                    binding.etStatusMessage.fairerEt.text.toString()
                )
            } else {
                viewModel.updateMe(
                    binding.etName.fairerEt.text.toString(),
                    viewModel.myData.value!!.profilePath,
                    binding.etStatusMessage.fairerEt.text.toString()
                )
            }

            it.findNavController().navigateUp()
        }

        binding.etStatusMessage.fairerEt.setOnTouchListener { status, _ ->
            status.requestFocus()
            if (binding.etStatusMessage.fairerEt.text.isNotEmpty()) {
                binding.stateIsTextChanged = true
            }
            false
        }

        binding.etName.fairerEt.setOnTouchListener { name, _ ->
            name.requestFocus()
            if (binding.etName.fairerEt.text.isNotEmpty()) {
                binding.nameIsTextChanged = true
            }
            false
        }

    }

    private fun initView() {
        binding.profileBtn.mainFooterButton.text = "입력 완료"
        binding.profileBtn.mainFooterButton.isEnabled = true
        binding.etStatusMessage.fairerEt.hint = getString(R.string.setting_profile_status_hint)
    }

    private fun ImageView.setImg(url: String?) {
        /*Glide.with(binding.ivImageview.context)
          .load(url)
          .placeholder(R.drawable.bg_profile_imageview_inactive)
          .error(R.drawable.bg_profile_imageview_inactive)
          .fitCenter()
          .override(84, 84)
          .into(binding.ivImageview)*/
        binding.ivImageview.load(url) {
            crossfade(true)
        }
    }


}