package com.depromeet.housekeeper.ui.join

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentInviteBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.util.NavigationUtil.navigateSafe
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.navigationInfoParameters
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class InviteFragment : Fragment() {
    lateinit var binding: FragmentInviteBinding
    lateinit var clipboard: ClipboardManager
    private val viewModel: InviteViewModel by viewModels()
    private val navArgs by navArgs<InviteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite, container, false)
        clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        bindingVm()
    }

    private fun bindingVm() {
        lifecycleScope.launchWhenCreated {
            viewModel.viewType.collect {
                if (it == InviteViewType.SIGN) {
                    viewModel.setCode(viewModel.groupName.value)
                } else {
                    viewModel.getInviteCodeResponse()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.inviteCodeValidPeriod.collect {
                binding.inviteValidPeriodTv.text = getString(
                    R.string.invite_code_valid_period_text,
                    viewModel.inviteCodeValidPeriod.value
                )
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.groupName.collect {
                binding.inviteGroupNameTv.text = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.groupName.collect {
                viewModel.groupName.value.apply {
                    val format = String.format(getString(R.string.invite_group_name_text), this)
                    val spannerString = SpannableString(format).apply {
                        setSpan(
                            ForegroundColorSpan(requireActivity().getColor(R.color.highlight)),
                            0,
                            this.indexOf("의"),
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    binding.inviteGroupNameTv.text = spannerString
                }
            }
        }
    }

    private fun initListener() {
        // invite fragment 분기 - 건너뛰기 유무
        viewModel.setViewType(navArgs.viewType)
        when (viewModel.viewType.value) {
            InviteViewType.SIGN -> {
                binding.inviteSkipBtn.visibility = View.VISIBLE
                binding.viewType = InviteViewType.SIGN
                viewModel.setGroupName(navArgs.houseName!!)
            }
            InviteViewType.SETTING -> {
                binding.inviteSkipBtn.visibility = View.GONE
                binding.viewType = InviteViewType.SETTING
            }
        }
        binding.inviteHeader.apply {
            defaultHeaderTitleTv.text = ""
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.inviteCopyBtn.setOnClickListener {
            onCopyToClipboard(viewModel.inviteCode.value)
        }

        binding.inviteKakaoShareBtn.setOnClickListener {
            onKakaoShare(requireContext())
        }

        binding.inviteSkipBtn.setOnClickListener {
            findNavController().navigateSafe(R.id.action_inviteFragment_to_mainFragment)
        }

    }

    private fun onCopyToClipboard(code: String) {
        val clip = ClipData.newPlainText("INVITE_CODE", code.toString())
        clipboard.setPrimaryClip(clip)

        Toast.makeText(
            requireContext(),
            getString(R.string.invite_code_copy_toast_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onKakaoShare(context: Context) {

        // TODO("템플릿 변경 필요")

        val defaultText = FeedTemplate(
            content = Content(
                title = getString(R.string.kakao_share_text),
                description = getString(R.string.kakao_share_description),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/fairer-def59.appspot.com/o/meta-images%2Finvite-code.png?alt=media&token=f6117459-e48b-41d2-8a61-642ac8ec7e56",
                imageWidth = 200,
                imageHeight = 200,
                link = Link(
                    webUrl = initDynamicLink().toString(),
                    mobileWebUrl = initDynamicLink().toString()
                )
            ),
            buttons = listOf(
                Button(
                    title = getString(R.string.kakao_share_button),
                    Link(
                        webUrl = initDynamicLink().toString(),
                        mobileWebUrl = initDynamicLink().toString()
                    )
                )
            )
        )


        // 카카오톡 설치여부 확인
        if (LinkClient.instance.isKakaoLinkAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            LinkClient.instance.defaultTemplate(
                context, defaultText
            ) { linkResult, error ->
                if (error != null) {
                    Timber.d("카카오톡 공유 실패 ${error.message}")
                } else if (linkResult != null) {
                    Timber.d("카카오톡 공유 성공 ${linkResult.intent}")
                    startActivity(linkResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Timber.d("Warning Msg: ${linkResult.warningMsg}")
                    Timber.d("Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.defaultTemplateUri(defaultText)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabs으로 Chrome 브라우저 열기
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // Chrome 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabs으로 디바이스 기본 브라우저 열기
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }


    private fun initDynamicLink(): Uri {
        val playStoreUri : Uri =Uri.parse(getString(R.string.play_store_fairer))
        val inviteCode = viewModel.inviteCode.value
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://faireran.com/?code=$inviteCode")
            domainUriPrefix = "https://faireran.page.link"
            androidParameters(requireContext().packageName) { fallbackUrl = playStoreUri }
            navigationInfoParameters { forcedRedirectEnabled = true }
        }
        val dynamicLinkUri = dynamicLink.uri
        Timber.d("dynamicUrl : $dynamicLinkUri")
        return dynamicLinkUri

    }
}