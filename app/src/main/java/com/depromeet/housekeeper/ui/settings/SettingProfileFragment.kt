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
import com.depromeet.housekeeper.util.EditTextUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingProfileFragment : BaseFragment<FragmentSettingProfileBinding>(R.layout.fragment_setting_profile) {
    private val viewModel: SettingProfileViewModel by viewModels()
    private val navArgs by navArgs<SettingProfileFragmentArgs>()

    override fun createView(binding: FragmentSettingProfileBinding) {

    }

    override fun onDestroyView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onDestroyView()
    }

    override fun viewCreated() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        validateName()
        initView()
        bindingVm()
        setListener()

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
        binding.settingProfileBackground.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                EditTextUtil.hideKeyboard(requireContext(), view)
            }
        }

        binding.etName.apply {
            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    binding.nameIsTextChanged = false
                    viewModel.setNameBackgroundBox(0)
                } else {
                    if (viewModel.nameData.value.isNotEmpty()) binding.nameIsTextChanged = true
                    viewModel.setNameBackgroundBox(1)
                    EditTextUtil.showKeyboard(requireContext(), this)
                }
            }
        }

        binding.etStatus.apply {
            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    binding.stateIsTextChanged = false
                    viewModel.setStatusBackgroundBox(0)
                } else {
                    if (viewModel.massageData.value.isNotEmpty()) binding.stateIsTextChanged = true
                    viewModel.setStatusBackgroundBox(1)
                    EditTextUtil.showKeyboard(requireContext(), this)
                }
            }
        }
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
        lifecycleScope.launchWhenCreated {
            viewModel.nameBackgroundBox.collect {
                when (it) {
                    1 -> { // edit 중
                        binding.clName.background =
                            resources.getDrawable(R.drawable.fairer_edit_text_focus_background)
                    }

                    2 -> { // error
                        binding.clName.background =
                            resources.getDrawable(R.drawable.edit_text_error_background)
                    }

                    else -> { // default
                        binding.clName.background =
                            resources.getDrawable(R.drawable.sign_name_edit_text_background)
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.statusBackgroundBox.collect {
                when (it) {
                    1 -> { // edit 중
                        binding.clAbout.background =
                            resources.getDrawable(R.drawable.fairer_edit_text_focus_background)
                    }

                    2 -> { // error
                        binding.clAbout.background =
                            resources.getDrawable(R.drawable.edit_text_error_background)
                    }

                    else -> { // default
                        binding.clAbout.background =
                            resources.getDrawable(R.drawable.sign_name_edit_text_background)
                    }
                }
            }
        }
    }


    private fun validateName() {
        binding.etName.addTextChangedListener {
            val nameValue: String = binding.etName.text.toString()
            viewModel.setNameData(nameValue)
            if (!nameValue.matches(EditTextUtil.INPUT_PATTERN.toRegex())) {
                binding.nameIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
                binding.signNameError.setText(R.string.sign_name_error)
                viewModel.setNameBackgroundBox(2)
            } else if(nameValue.length>5) {
                binding.nameIsError = true
                binding.profileBtn.mainFooterButton.isEnabled = false
                binding.signNameError.setText(R.string.sign_name_text_over_error)
                viewModel.setNameBackgroundBox(2)
            }else {
                binding.nameIsError = false
                binding.profileBtn.mainFooterButton.isEnabled =
                    nameValue.isNotEmpty()
                viewModel.setNameBackgroundBox(1)
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
                 viewModel.setStatusBackgroundBox(2)
             }else {
                binding.stateIsError = false
                binding.profileBtn.mainFooterButton.isEnabled = true
                 viewModel.setStatusBackgroundBox(1)
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
    }


    private fun ImageView.setImg(url: String?) {
        binding.ivImageview.load(url) {
            crossfade(true)
        }
    }


}
