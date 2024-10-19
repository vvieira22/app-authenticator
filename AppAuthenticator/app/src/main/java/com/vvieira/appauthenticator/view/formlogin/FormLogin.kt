package com.vvieira.appauthenticator.view.formlogin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.vvieira.appauthenticator.databinding.ActivityFormCadastroBinding
import com.vvieira.appauthenticator.databinding.ActivityFormLoginBinding

lateinit var binding: ActivityFormLoginBinding
private var dialogCadastroExibido = false

class FormLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)

        binding.botaoCadastrar.setOnClickListener {
            if (!dialogCadastroExibido) {
                dialogCadastro(this, binding.root)
                dialogCadastroExibido = true
            }
        }
        binding.botaoLogin.setOnClickListener {
            callSnackBar(
                it,
                "Logado com sucesso!",
                Color.BLUE,
                Color.WHITE
            )
        }
        setContentView(binding.root)
    }

    private fun dialogCadastro(contexto: Context, view: View) {
        var auth = FirebaseAuth.getInstance()
        val binding = ActivityFormCadastroBinding.inflate(LayoutInflater.from(contexto))
        val builder = AlertDialog.Builder(contexto)
            .setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.setOnDismissListener {
            dialogCadastroExibido = false
        }
        dialog.show()

        binding.botaoCadastrar.setOnClickListener {
            val nome = binding.nomeCadastro.text.toString()
            val doc = binding.documento.text.toString()
            val email = binding.email.text.toString()
            val senha = binding.senhaCadastro.text.toString()

            val valCampos = validarCamposCadastro(nome, doc, email, senha)

            if (valCampos.isEmpty()) {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            callSnackBar(
                                view,
                                "Cadastro realizado com sucesso!",
                                Color.GREEN,
                                Color.WHITE
                            )
                            dialog.dismiss()
                        } else {
                            val exception = cadastro.exception as? FirebaseAuthException
                            var mensagemErro = exception?.errorCode
                            when (mensagemErro) {
                                "ERROR_INVALID_EMAIL" -> {
                                    mensagemErro = "Email inválido"
                                }

                                "ERROR_WEAK_PASSWORD" -> {
                                    mensagemErro = "Senha fraca"
                                }

                                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                    mensagemErro = "Email já cadastrado"
                                }
                            }
                            callSnackBar(
                                binding.root,
                                mensagemErro.toString(),
                                Color.RED,
                                Color.WHITE
                            )
                        }
                    }
            } else {
                callSnackBar(
                    binding.root,
                    valCampos,
                    Color.RED,
                    Color.WHITE
                )
            }
        }
    }

    private fun validarCamposCadastro(
        nome: String,
        doc: String,
        email: String,
        senha: String
    ): String {
        try {
            if (nome.isEmpty())
                throw Exception("Campo nome não pode ser vazio.")
            else if (doc.isEmpty())
                throw Exception("Campo documento não pode ser vazio.")
            else if (email.isEmpty())
                throw Exception("Campo email não pode ser vazio.")
            else if (senha.isEmpty())
                throw Exception("Campo senha não pode ser vazio.")
            return ""
        } catch (e: Exception) {
            return e.message.toString()
        }
    }

    private fun callSnackBar(view: View, mensagem: String, cor: Int, corTexto: Int) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(cor)
        snackbar.setTextColor(corTexto)
        snackbar.show()
    }
}