package com.depromeet.housekeeper.ui

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.MainActivity
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentInviteBinding
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import timber.log.Timber

class InviteFragment : Fragment() {
    lateinit var binding: FragmentInviteBinding
    lateinit var clipboard: ClipboardManager
    private val viewModel: InviteViewModel by viewModels()

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
    }

    private fun initListener() {

        binding.inviteHeader.apply {
            defaultHeaderTitleTv.text = ""

            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.inviteCopyBtn.setOnClickListener {
            //onCopyToClipboard(viewModel.inviteCode.value)
            onCopyToClipboard(initDynamicLink())
        }

        binding.inviteKakaoShareBtn.setOnClickListener {
            onKakaoShare(requireContext(),initDynamicLink())
        }

        binding.inviteSkipBtn.setOnClickListener {
            findNavController().navigate(R.id.action_inviteFragment_to_mainFragment)
        }

    }

    private fun onCopyToClipboard(code: Uri) {
        val clip = ClipData.newPlainText("INVITE_CODE", code.toString())
        clipboard.setPrimaryClip(clip)

        val toast = Toast.makeText(requireContext(), "코드를 클립보드에 복사했습니다.", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL)
        toast.show()
    }

    private fun onKakaoShare(context: Context, uri: Uri) {

        // TODO("템플릿 변경 필요")
        val defaultText = TextTemplate(
            text = """
            공유할 텍스트
        """.trimIndent(),
            link = Link(
                webUrl = "https://developers.kakao.com",
                mobileWebUrl = "https://developers.kakao.com"
            )
        )

        // 카카오톡 설치여부 확인
        if (LinkClient.instance.isKakaoLinkAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            LinkClient.instance.defaultTemplate(context, defaultText) { linkResult, error ->
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

    private fun initDynamicLink() : Uri {
        val inviteCode = viewModel.inviteCode.value
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://faireran.com/?code=$inviteCode")
            domainUriPrefix = "https://faireran.page.link"
            androidParameters(requireContext().packageName) {}
            navigationInfoParameters { forcedRedirectEnabled = true }
        }
        val dynamicLinkUri = dynamicLink.uri
        Timber.d("dynamicUrl : $dynamicLinkUri")
        return dynamicLinkUri

    }
}