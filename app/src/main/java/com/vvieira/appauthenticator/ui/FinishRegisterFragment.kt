package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vvieira.appautenticator.utils.CpfCnpjTextWatcher
import com.vvieira.appauthenticator.databinding.FragmentFinishRegisterBinding
import com.vvieira.appauthenticator.util.MyMaskGeneric
import com.vvieira.appauthenticator.util.TelefoneBrasilTextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishRegisterFragment : Fragment() {

    private var _binding: FragmentFinishRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        requireActivity().enableEdgeToEdge() //acho que n precisa desse cara se ja ta ativado na activity que chamou ele.
        _binding = FragmentFinishRegisterBinding.inflate(inflater, container, false)
        binding.botaoVoltar.setOnClickListener {
            requireActivity().finish()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dataLoginWelcome.observe(viewLifecycleOwner) { data ->
            binding.email.setText(data.toString())
        }
        binding.telefone.addTextChangedListener(MyMaskGeneric(binding.telefone, "(##)#####-####"))
        binding.dataNascimento.addTextChangedListener(MyMaskGeneric(binding.dataNascimento, "##/##/####"))
        binding.documento.addTextChangedListener(CpfCnpjTextWatcher())
    }

    override fun onDestroyView() {
//        activity?.recreate()
        super.onDestroyView()
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }
}