package com.vvieira.appauthenticator.ui.register

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vvieira.appauthenticator.databinding.FragmentCadastroBinding
import com.vvieira.appauthenticator.domain.model.Register
import com.vvieira.appauthenticator.ui.AuthenticViewModel
import com.vvieira.appauthenticator.util.DEFAUT_AUTH
import com.vvieira.appauthenticator.util.Utils
import com.vvieira.appauthenticator.util.Utils.Companion.customSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var collectJob: Job? = null

    private var loadingDialog: Dialog? = null

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
        startCollectData()
    }

    private fun observerViewModelEvents() {
        viewModel.nameField.observe(viewLifecycleOwner) { resposta ->
            binding.nomeCadastro.error = resposta
        }
        viewModel.documentField.observe(viewLifecycleOwner) { resposta ->
            binding.documento.error = resposta
        }
        viewModel.emailRegisterField.observe(viewLifecycleOwner) { resposta ->
            binding.emailRegister.error = resposta
        }
        viewModel.phoneField.observe(viewLifecycleOwner) { resposta ->
            binding.telefone.error = resposta
        }
        viewModel.passwordRegisterField.observe(viewLifecycleOwner) { resposta ->
            binding.senhaCadastro.error = resposta
        }

        //TODO PARAMETRIZAR COR E CONFIGURACAO DA SNACKBAR PARA DIFERENCIAR EVENTOS.
        viewModel.registerResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response.code) {
                    in 200..299 -> customSnackBar(
                        binding.root,
                        response.message,
                        Color.GREEN,
                        Color.WHITE
                    )

                    in 400..499 -> customSnackBar(
                        binding.root,
                        response.message,
                        Color.RED,
                        Color.BLACK
                    )

                    else -> customSnackBar(
                        binding.root,
                        response.message,
                        Color.RED,
                        Color.WHITE
                    )
                }
            }
        }
    }

    private fun setListeners() {
        binding.botaoCadastrar.setOnClickListener {
            lifecycleScope.launch {
                viewModel.registerPass(
                    Register(
                        email = binding.emailRegister.text.toString(),
                        password = binding.senhaCadastro.text.toString(),
                        name = binding.nomeCadastro.text.toString(),
                        document = binding.documento.text.toString(),
                        phone = binding.telefone.text.toString(),
//                        birthDate = binding.dataNascimento.text.toString(),
                        type = DEFAUT_AUTH
                    ),
                    requireContext()
                )
            }
        }

        binding.botaoVoltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun startCollectData() {
        collectJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerFormState.collect { valor ->
                    if (valor.isLoading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.documento.error = null
        super.onDestroyView()
        collectJob?.cancel() // Importante: Cancelar a coleta para evitar vazamentos
        collectJob = null
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
