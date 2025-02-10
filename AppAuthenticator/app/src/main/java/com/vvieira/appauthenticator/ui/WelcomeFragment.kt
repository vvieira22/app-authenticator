package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vvieira.appauthenticator.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        requireActivity().enableEdgeToEdge() //acho que n precisa desse cara se ja ta ativado na activity que chamou ele.
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.botaoVoltar.setOnClickListener {
            requireActivity().finish()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dataLoginWelcome.observe(viewLifecycleOwner) { data ->
            binding.email.text = data.toString()
        }
    }
//
//    //RESPEITAR PADDING.
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
//            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
//
//            // Ajuste o padding considerando os system bars e o IME (teclado)
//            v.setPadding(
//                systemBarsInsets.left,
//                0,
//                systemBarsInsets.right,
//                systemBarsInsets.bottom + imeInsets.bottom
//            )
//
//            WindowInsetsCompat.CONSUMED
//        }
//    }

    override fun onDestroyView() {
//        activity?.recreate()
        super.onDestroyView()
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }
}