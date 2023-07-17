package com.depromeet.housekeeper.ui.settings

import android.view.WindowManager
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentSettingProfileBinding
import com.depromeet.housekeeper.model.enums.ProfileViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class SettingProfileFragment : BaseFragment<FragmentSettingProfileBinding>(R.layout.fragment_setting_profile) {
    private val viewModel: SettingProfileViewModel by viewModels()
    private val navArgs by navArgs<SettingProfileFragmentArgs>()

    override fun createView(binding: FragmentSettingProfileBinding) {

    }

    override fun viewCreated() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        initView()
        bindingVm()
        setListener()
        validateName()

        val url: String = when {
            !navArgs.profilePath.isNullOrEmpty() -> {
                navArgs.profilePath!!
            }
            else -> viewModel.profileData.value
        }
        Glide.with(requireContext())
            .load(url)
            .placeholder(requireContext().getDrawable(R.drawable.bg_profile_imageview_inactive))
            .into(binding.ivImageview)

        binding.nameIsTextChanged = false
        binding.stateIsTextChanged = false
    }

    private fun initView() {
        binding.profileBtn.mainFooterButton.text = "입력 완료"
        binding.profileBtn.mainFooterButton.isEnabled = true
        binding.etStatus.hint = getString(R.string.setting_profile_status_hint)
        binding.layoutNetwork.llDisconnectedNetwork.bringToFront()

    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.profileData.collect {
                val url: String = when {
                    !navArgs.profilePath.isNullOrEmpty() -> {
                        navArgs.profilePath!!
                    }
                    else -> viewModel.profileData.value
                }
                Glide.with(requireContext())
                    .load(url)
                    .placeholder(requireContext().getDrawable(R.drawable.bg_profile_imageview_inactive))
                    .into(binding.ivImageview)

                binding.nameIsTextChanged = false
                binding.stateIsTextChanged = false
                }
            }

        lifecycleScope.launchWhenCreated {
            viewModel.networkError.collect {
                binding.layoutNetwork.isNetworkError = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.nameData.collectLatest {
                binding.name = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.massageData.collectLatest {
                binding.status = it
            }
        }
    }

    private fun validateName() {
        val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"
        binding.etName.addTextChangedListener {
            val nameValue: String = binding.etName.text.toString()
            viewModel.setNameData(nameValue)
            if (!nameValue.matches(pattern.toRegex())) {
                binding.nameIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
                binding.signNameError.setText(R.string.sign_name_error)
            } else if(nameValue.length>5) {
                binding.nameIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
                binding.signNameError.setText(R.string.sign_name_text_over_error)
            }else {
                binding.nameIsError = false
                binding.profileBtn.mainFooterButton.isEnabled =
                    nameValue.isNotEmpty()
            }
            binding.nameIsTextChanged = nameValue != ""
        }


        binding.etStatus.addTextChangedListener {
            val value: String = binding.etStatus.text.toString()
            viewModel.setMassageData(value)
             if(value.length>20) {
                binding.stateIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
                 binding.signNameError.setText(R.string.setting_profile_text_over_error)
             }else {
                binding.stateIsError = false
                binding.profileBtn.mainFooterButton.isEnabled = true
            }
            binding.stateIsTextChanged = value != ""
        }
    }

    private fun setListener() {
        binding.settingProfileBackground.setOnClickListener {
            binding.nameIsTextChanged = false
            binding.stateIsTextChanged = false
            binding.etName.isEnabled = false
            binding.etStatus.isEnabled = false
            binding.etName.isEnabled = true
            binding.etStatus.isEnabled = true
        }
        binding.signNameClear.setOnClickListener {
            binding.etName.setText(R.string.sign_name_blank)
        }
        binding.statusClear.setOnClickListener {
            binding.etStatus.setText(R.string.sign_name_blank)
        }

        binding.lvProfileImageview.setOnClickListener {
            Timber.d("asd ${viewModel.nameData.value}")
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
            val newMemberName = binding.etName.text.toString()
            val newProfilePath = if (navArgs.profilePath != null) {
                navArgs.profilePath
            } else {
                viewModel.profileData.value
            }
            val newStatusMessage = binding.etStatus.text.toString()

            viewModel.setProfile(newMemberName, newProfilePath!!, newStatusMessage)
            viewModel.updateMe(newMemberName, newProfilePath, newStatusMessage)

            it.findNavController().navigateUp()
        }

        binding.etStatus.setOnTouchListener { status, _ ->
            status.requestFocus()
            if (binding.etStatus.text.isNotEmpty()) {
                binding.stateIsTextChanged = true
            }
            false
        }

        binding.etName.setOnTouchListener { name, _ ->
            name.requestFocus()
            if (binding.etName.text.isNotEmpty()) {
                binding.nameIsTextChanged = true
            }
            false
        }

    }


    private fun ImageView.setImg(url: String?) {
        binding.ivImageview.load(url) {
            crossfade(true)
        }
    }


}
