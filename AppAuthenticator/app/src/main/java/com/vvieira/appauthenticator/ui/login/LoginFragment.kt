package com.vvieira.appauthenticator.ui.login

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.FragmentLoginBinding
import com.vvieira.appauthenticator.util.Utils
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var loadingDialog: Dialog? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticViewModel by activityViewModels()

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
            viewModel.loginPassword(
                binding.loginField.text.toString(),
                binding.senhaLogin.text.toString()
            )
            hideLoading()
        }
//
//        binding.recuperarSenha.setOnClickListener {
//            showLoading()
//            Handler(Looper.getMainLooper()).postDelayed({
//                hideLoading()
//            }, 5000)
//        }
    }

    private fun observerViewModelEvents() {
        viewModel.userField.observe(viewLifecycleOwner) { resposta ->
            binding.loginField.error = resposta
        }
        viewModel.passwordField.observe(viewLifecycleOwner) { resposta ->
            binding.senhaLogin.error = resposta
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) { resposta ->
            //a resposta tem o token, assim que ir para tela de acesso
            //passar esse token junto.
            if (resposta != "") {
                customSnackBar(
                    binding.root,
                    resposta!!,
                    Color.GREEN,
                    Color.WHITE
                )
            } else {
                Log.d("LOGIN RESPONSE", resposta.toString())
                customSnackBar(
                    binding.root,
                    getString(R.string.invalid_credential),
                    Color.RED,
                    Color.WHITE
                )
            }
        }
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
