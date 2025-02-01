package com.vvieira.appauthenticator.ui.login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.vvieira.appauthenticator.BuildConfig
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.FragmentLoginBinding
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.ui.AuthenticViewModel
import com.vvieira.appauthenticator.util.GOOGLE_AUTH
import com.vvieira.appauthenticator.util.Utils
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var credentialManager: CredentialManager

    private var loadingDialog: Dialog? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticViewModel by activityViewModels()

    // eu so tenho context depois do onAttach.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        credentialManager = CredentialManager.create(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.textCadastrar.setOnClickListener {
            val navController = it.findNavController()
            navController.navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        observerViewModelEvents()
    }

    private fun setListeners() {
        binding.botaoLogin.setOnClickListener {
            showLoading()
            lifecycleScope.launch {
                viewModel.loginPassword(
                    binding.loginField.text.toString(),
                    binding.senhaLogin.text.toString()
                ).join()// Aguarda a conclusão do Job
                hideLoading()
            }
        }

//        binding.recuperarSenha.setOnClickListener {
//            showLoading()
//            Handler(Looper.getMainLooper()).postDelayed({
//                hideLoading()
//            }, 5000)
//        }
        binding.gmailLogin.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val googleIdOption: GetSignInWithGoogleOption =
                        GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CREDENTIAL_USER)
                            .build()
                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val credentialManager = CredentialManager.create(requireContext())
                    val response = credentialManager.getCredential(requireContext(), request)

                    when (val result = response.credential) {
                        is GoogleIdTokenCredential -> {
                            val id = result.id
                            Log.i("GOOGLE-ID", id)
                            val displayName = result.displayName.toString()
                            Log.i("GOOGLE-NAME", displayName.toString())
                            val photoUri = result.profilePictureUri
                            Log.i("GOOGLE-PHOTO", photoUri.toString())
                            val token = result.idToken
                            Log.i("GOOGLE-TOKEN", token)

                            try {
                                val user = RegisterModelRequest(
                                    email = id,
                                    name = displayName,
                                    gmail_id = result.idToken
                                )
                                viewModel.registerWithGoogle(user, GOOGLE_AUTH)
                                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                            } catch (e: Exception) {
                                Log.d("Erro AuthenticViewModel: ", e.message.toString())
                            }
                            credentialManager.clearCredentialState(ClearCredentialStateRequest())
                        }

                        else -> {
                            // Catch any unrecognized credential type here.
                            Log.i("GOOGLE", "Unexpected type of credential")
                        }
                    }
                } catch (e: GetCredentialException) {
                    when (e) {
                        is NoCredentialException -> {
                            Log.e("NoCredentialException", "No matching credential found.")
                        }

                        else -> {
                            Log.e("Exception", e.localizedMessage ?: "Unknown error")
                        }
                    }
                }
                viewModel.userMessageShown()
            }
        }
    }

    private fun observerViewModelEvents() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.teste == "caralho") {
                        customSnackBar(
                            binding.root,
                            uiState.teste,
                            Color.RED,
                            Color.WHITE
                        ).let{
                            viewModel.userMessageShown()
                        }
                    }
                }
            }
        }
//
//        viewModel.userField.observe(viewLifecycleOwner) { resposta ->
//            binding.loginField.error = resposta
//        }
//        viewModel.passwordField.observe(viewLifecycleOwner) { resposta ->
//            binding.senhaLogin.error = resposta
//        }

//        viewModel.loginResponse.observe(viewLifecycleOwner) { event ->
//            event.getContentIfNotHandled()?.let { resposta ->
//                if (resposta.isNotEmpty()) {
//                    customSnackBar(
//                        binding.root,
//                        resposta,
//                        Color.GREEN,
//                        Color.WHITE
//                    )
//                } else {
//                    Log.d("LOGIN RESPONSE", resposta)
//                    customSnackBar(
//                        binding.root,
//                        getString(R.string.invalid_credential),
//                        Color.RED,
//                        Color.WHITE
//                    )
//                }
//            }
//        }
//
//        viewModel.registerResponse.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                null -> {
//                    customSnackBar(
//                        binding.root,
//                        "not ok",
//                        Color.RED,
//                        Color.WHITE
//                    )
//                }
//
//                else -> {
//                    customSnackBar(
//                        binding.root,
//                        "OK",
//                        Color.GREEN,
//                        Color.WHITE
//                    )
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        loadingDialog = Utils.showLoadingDialog(requireActivity())
    }
}
