package com.vvieira.appauthenticator.ui.register

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.FragmentCadastroBinding
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.ui.AuthenticViewModel
import com.vvieira.appauthenticator.util.DEFAUT_AUTH
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticViewModel by activityViewModels() //nao to usando activityViewModels() pra vincular a viewmodel a esse framgneto, quando morrer ja era

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        observerViewModelEvents()
    }

    private fun observerViewModelEvents() {
        viewModel.nameField.observe(viewLifecycleOwner) { resposta ->
            binding.nomeCadastro.error = resposta
        }
        viewModel.documentField.observe(viewLifecycleOwner) { resposta ->
            binding.documento.error = resposta
        }
        viewModel.emailField.observe(viewLifecycleOwner) { resposta ->
            binding.email.error = resposta
        }
        viewModel.phoneField.observe(viewLifecycleOwner) { resposta ->
            binding.telefone.error = resposta
        }
        viewModel.passwordRegisterField.observe(viewLifecycleOwner) { resposta ->
            binding.senhaCadastro.error = resposta
        }
        viewModel.registerResponse.observe(viewLifecycleOwner) { resposta ->
            if (resposta != "") {
                customSnackBar(
                    binding.root,
                    resposta!!,
                    Color.GREEN,
                    Color.WHITE
                ).let {
                    viewModel.userMessageShown()
                }
            } else {
                Log.d("REGISTER RESPONSE", resposta.toString())
                customSnackBar(
                    binding.root,
                    getString(R.string.invalid_credential),
                    Color.RED,
                    Color.WHITE
                ).let {
                    viewModel.userMessageShown()
                }
            }
        }
    }

    private fun setListeners() {
        binding.botaoCadastrar.setOnClickListener {
            viewModel.registerPass(
                RegisterModelRequest(
                    email = binding.email.text.toString(),
                    password = binding.senhaCadastro.text.toString(),
                    name = binding.nomeCadastro.text.toString(),
                    document = binding.documento.text.toString(),
                    telefone = binding.telefone.text.toString(),
                ),
                DEFAUT_AUTH
            )
        }

        binding.botaoVoltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }

}
