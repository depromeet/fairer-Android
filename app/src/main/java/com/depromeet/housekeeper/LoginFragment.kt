package com.depromeet.housekeeper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.depromeet.housekeeper.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber



class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val RC_SIGN_IN = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        Googlelogin()
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.signInButton.setOnClickListener {
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            //이미 로그인 되어 있을시
            viewModel.getTokens()
            Timber.d("accesstoken : ${viewModel.AccessToken.value}")
            Timber.d("refreshtoken : ${viewModel.RefreshToken.value}")
            navigateToMain()
        }
    }

    private fun Googlelogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/userinfo.email"),Scope("https://www.googleapis.com/auth/userinfo.profile"),Scope("openid"))
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(binding.root.context, gso)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val authCode = account.serverAuthCode

                if (authCode != null) {
                    viewModel.getAuthcode(authCode)
                    Timber.d("auth code : ${viewModel.AuthCode.value}")
                }
                viewModel.getLoginResponse()
                navigateToMain()
            } catch (e: ApiException) {
                Timber.w("failed")
            }
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

}