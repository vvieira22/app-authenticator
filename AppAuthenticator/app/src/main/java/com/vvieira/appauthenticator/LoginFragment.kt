package com.vvieira.appauthenticator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vvieira.appauthenticator.databinding.LoginBinding

class LoginFragment: Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginBinding.inflate(inflater, container, false)

        binding.botaoCadastrar.setOnClickListener {
            dialogCadastro(requireContext())
        }
        return binding.root
    }
}

private fun dialogCadastro(contexto : Context) {
    val dialogView =
        LayoutInflater.from(contexto).inflate(R.layout.cadastrar_usuario, null)
    val builder = AlertDialog.Builder(contexto)
        .setView(dialogView)
    val dialog = builder.create()

    // Configurar o background do Dialog
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    // Encontrar os elementos do layout do Dialog (se necessário)
    val nomeEditText = dialogView.findViewById<EditText>(R.id.nome_cadastro)
    val usuarioEditText = dialogView.findViewById<EditText>(R.id.usuario_cadastro)
    val senhaEditText = dialogView.findViewById<EditText>(R.id.senha_cadastro)
    val botaoCadastrar = dialogView.findViewById<Button>(R.id.botao_cadastrar)

    // Configurar ações do botão Cadastrar
    botaoCadastrar.setOnClickListener {
        // Lógica de cadastro aqui (ex: salvar dados, etc.)
        // ...
        dialog.dismiss() // Fechar o Dialog após o cadastro
    }

    dialog.show()
}
