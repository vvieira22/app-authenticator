package com.vvieira.appauthenticator.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.FragmentLoginBinding
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import com.vvieira.appauthenticator.util.Utils.Companion.valEmail
import com.vvieira.appauthenticator.util.Utils.Companion.valPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners(view)
        observerViewModelEvents()
    }

    private fun setListeners(view: View) {
        binding.botaoLogin.setOnClickListener {
            try {
                var passValidation = true
                if (binding.loginField.text.toString().isEmpty()) {
                    passValidation = false
                    binding.loginField.error = "Campo vazio"
                } else {
                    if (!valEmail(binding.loginField.text.toString())) {
                        passValidation = false
                        binding.loginField.error = "Email inválido"
                    }
                }
                if (binding.senhaLogin.text.toString().isEmpty()) {
                    passValidation = false
                    binding.senhaLogin.error = "Campo vazio"
                } else {
                    if (!valPassword(binding.senhaLogin.text.toString())) {
                        passValidation = false
                        binding.senhaLogin.error = "Senha menor que 6 digitos"
                    }
                }
                if (passValidation) {
                    viewModel.loginPassword(
                        binding.loginField.text.toString(),
                        binding.senhaLogin.text.toString()
                    )
                }
            } catch (e: Exception) {
                Log.d("LOGIN VAL ERROR", e.message.toString())
            }
        }
    }

    private fun observerViewModelEvents() {
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
}