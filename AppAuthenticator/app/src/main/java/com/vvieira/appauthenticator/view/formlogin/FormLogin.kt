package com.vvieira.appauthenticator.view.formlogin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.ActivityFormCadastroBinding
import com.vvieira.appauthenticator.databinding.ActivityFormLoginBinding

lateinit var binding: ActivityFormLoginBinding
private lateinit var callbackManager: CallbackManager

private var dialogCadastroExibido = false
var auth : FirebaseAuth? = null

class FormLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityFormLoginBinding.inflate(layoutInflater)

        callbackManager = CallbackManager.Factory.create()

        binding.facebookLogin.setOnClickListener {
           loginFacebook(auth!!)
        }

        binding.textCadastrar.setOnClickListener{
            if (!dialogCadastroExibido) {
                dialogCadastro(this, binding.root, auth!!)
                dialogCadastroExibido = true
            }
        }

        binding.botaoLogin.setOnClickListener {
            if (doLogin(binding.emailLogin.text.toString(), binding.senhaLogin.text.toString(),
                    auth!!
                )) {
                callSnackBar(
                    it,
                    getString(R.string.login_sucesso),
                    Color.BLUE,
                    Color.WHITE
                )
            }
        }
        setContentView(binding.root)
    }

    private fun loginFacebook(auth: FirebaseAuth) : Boolean {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FacebookLogin", "Login bem-sucedido: ${result.accessToken}")
                handleFacebookAcessToken(result.accessToken, auth)
            }

            override fun onCancel() {
                Log.e("[ERRO]", "handleFacebookAcessToken(): ")
            }

            override fun onError(error: FacebookException) {
                Log.e("[ERRO]", "handleFacebookAcessToken(): " + error.message.toString())
            }
        })
        return false
    }

    private fun handleFacebookAcessToken(loginResult: AccessToken, auth: FirebaseAuth){
        //get credentials
        val credential = FacebookAuthProvider.getCredential(loginResult.token)
        //sign in with credentials
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val email = it.user?.email
                Toast.makeText(this, "Login efetuado com sucesso: " + email, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Log.e("[ERRO]", "handleFacebookAcessToken(): " + e.message.toString())
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun dialogCadastro(contexto: Context, view: View, auth: FirebaseAuth) {
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
                                getString(R.string.cadastro_sucesso),
                                Color.GREEN,
                                Color.WHITE
                            )
                            dialog.dismiss()
                        } else {
                            val exception = cadastro.exception as? FirebaseAuthException
                            var mensagemErro = exception?.errorCode
                            when (mensagemErro) {
                                "ERROR_INVALID_EMAIL" -> mensagemErro = getString(R.string.email_invalido)
                                "ERROR_WEAK_PASSWORD" -> mensagemErro = getString(R.string.senha_fraca)
                                "ERROR_EMAIL_ALREADY_IN_USE" -> mensagemErro = getString(R.string.email_em_uso)
                                else -> mensagemErro = "Erro desconhecido: $mensagemErro"
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
            if (nome.isEmpty()) throw Exception(getString(R.string.nome_vazio))
            else if (doc.isEmpty()) throw Exception(getString(R.string.cpf_cnpj_vazio))
            else if (email.isEmpty()) throw Exception(getString(R.string.email_vazio))
            else if (senha.isEmpty()) throw Exception(getString(R.string.senha_vazio))
            return ""
        } catch (e: Exception) {
            Log.e("[ERRO]", "validarCamposCadastro(): " + e.message.toString())
            return e.message.toString()
        }
    }

    private fun callSnackBar(view: View, mensagem: String, cor: Int, corTexto: Int) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(cor)
        snackbar.setTextColor(corTexto)
        snackbar.show()
    }

    private fun doLogin(email: String, senha: String, auth: FirebaseAuth): Boolean {
        var pass = false

        if (email.isEmpty() || senha.isEmpty()) {
            callSnackBar(
                binding.root,
                getString(R.string.campos_vazios),
                Color.RED,
                Color.WHITE
            )
        } else {
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
                autenticacao ->
                    if (autenticacao.isSuccessful) {
                        pass = true
                        callSnackBar(
                            binding.root,
                            getString(R.string.login_sucesso),
                            Color.BLUE,
                            Color.WHITE)
                    }
                }.addOnFailureListener { exception ->
                    var mensagemErro = exception.message
                    when (exception) {
                        is FirebaseNetworkException -> mensagemErro = getString(R.string.network_request_failed)
                        is FirebaseAuthInvalidCredentialsException -> mensagemErro = getString(R.string.invalid_credential)
                        else -> mensagemErro = "Erro desconhecido: $mensagemErro"
                    }
                    Log.e("[ERRO]", "doLogin(): $mensagemErro")
                    callSnackBar(
                        binding.root,
                        mensagemErro,
                        Color.RED,
                        Color.WHITE
                    )
                }
            }
        return pass
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}