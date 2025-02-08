package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.vvieira.appauthenticator.databinding.FragmentAcceptTermsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcceptTermsFragment : Fragment() {

    private var _binding: FragmentAcceptTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        requireActivity().enableEdgeToEdge() //acho que n precisa desse cara se ja ta ativado na activity que chamou ele.
        _binding = FragmentAcceptTermsBinding.inflate(inflater, container, false)

        return binding.root
    }

    //RESPEITAR PADDING.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            // Ajuste o padding considerando os system bars e o IME (teclado)
            v.setPadding(
                systemBarsInsets.left,
                0,
                systemBarsInsets.right,
                systemBarsInsets.bottom + imeInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpa o binding para evitar vazamentos de memória
    }
}