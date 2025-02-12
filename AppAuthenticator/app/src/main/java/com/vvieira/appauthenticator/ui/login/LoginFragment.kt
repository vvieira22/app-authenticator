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
import com.vvieira.appauthenticator.domain.model.Login
import com.vvieira.appauthenticator.domain.model.Register
import com.vvieira.appauthenticator.ui.AuthenticViewModel
import com.vvieira.appauthenticator.util.DEFAUT_AUTH
import com.vvieira.appauthenticator.util.GOOGLE_AUTH
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_FACEBOOK_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_GOOGLE_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.NOT_REGISTERED_YET
import com.vvieira.appauthenticator.util.Utils
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var credentialManager: CredentialManager
    private var collectJob: Job? = null

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
        startCollectData()
    }

    private fun setListeners() {
        binding.botaoLogin.setOnClickListener {
//            showLoading()
            lifecycleScope.launch {
                viewModel.loginPassword(
                    Login(
                        email = binding.loginField.text.toString(),
                        password = binding.senhaLogin.text.toString(),
                        type = DEFAUT_AUTH
                    ),
                    requireContext()
                )
//                ).join()// Aguarda a conclusão do Job
//                hideLoading()
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
                                val user = Register(
                                    email = id,
                                    name = displayName,
                                    gmailId = result.idToken,
                                    type = GOOGLE_AUTH
                                )
                                viewModel.socialAuth(user, requireContext())
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
            }
        }

        binding.recuperarSenha.setOnClickListener {
            val navController = it.findNavController()
            navController.navigate(R.id.action_loginFragment_to_acceptTermsFragment)
        }
    }

    private fun observerViewModelEvents() {
        viewModel.emaiLoginField.observe(viewLifecycleOwner) { resposta ->
            binding.loginField.error = resposta
        }

        viewModel.passwordLoginField.observe(viewLifecycleOwner) { resposta ->
            binding.senhaLogin.error = resposta
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response.code) {
                    in 200..299 -> {
                        val navController = requireView().findNavController()
                        navController.navigate(R.id.action_loginFragment_to_welcomeFragment)
                    }

                    else -> customSnackBar(
                        binding.root,
                        response.message,
                        Color.RED,
                        Color.WHITE
                    )
                }
            }
        }

        viewModel.socialResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                when (message) {
                    NOT_REGISTERED_YET -> {
                        //TODO FAZER CADASTRAMENTO, INDO PARA OUTRO FRAGMENTO COM INFORMACOES E FALANDO PRA ELE LER TERMO DE USO.
                    }

                    ALREADY_GOOGLE_REGISTERED -> {
                        //TODO, REALIZAR LOGIN.
                    }

                    ALREADY_FACEBOOK_REGISTERED -> {
                        //TODO, REALIZAR LOGIN.
                    }

                    (ALREADY_GOOGLE_REGISTERED + ALREADY_FACEBOOK_REGISTERED) -> {
                        //TODO FALAR QUE A CONTA JA ESTA VINCULADA A UM EMAIL E SENHA, FAZER LOGIN ACIMA. ALGO ASSIM
                    }

                    else -> {
                        customSnackBar(
                            binding.root,
                            message,
                            Color.RED,
                            Color.WHITE
                        )
                    }
                }
            }
        }

        viewModel.socialAuthInformations.observe(viewLifecycleOwner) { loginInf ->
            customSnackBar(
                binding.root,
                "CADASTRAR!",
                Color.GREEN,
                Color.WHITE
            )
        }

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

    //AQUI BOTAMOS PARA CHAMAR LOADING, MAS POSSO PEGAR DADOS E IR PARA OUTRA ACTIVITY DEPOIS DO LOGIN.
    private fun startCollectData() {
        collectJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginFormState.collect { valor ->
                        if (valor.isLoading) {
                            showLoading()
                        } else {
                            hideLoading()
                        }
                    }
                }
                launch {
                    viewModel.socialFormState.collect { valor ->
                        if (valor.isLoading) {
                            showLoading()
                        } else {
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        collectJob?.cancel() // Importante: Cancelar a coleta para evitar vazamentos
        collectJob = null
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }

    //TODO TALVEZ COLOCAR NA BASEFRAGMENT showLoading e hideLoading..
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        loadingDialog = Utils.showLoadingDialog(requireActivity())
    }
}
