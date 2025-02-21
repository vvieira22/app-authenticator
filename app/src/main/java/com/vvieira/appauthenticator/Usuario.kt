package com.vvieira.appauthenticator

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class Usuario(val nome : String, val cpf : String, val email : String, val telefone : String) {
    override fun toString(): String {
        return "\n{nome='$nome',\n cpf='$cpf',\n email='$email',\n telefone='$telefone'\n}"
    }
    private var db = FirebaseFirestore.getInstance()

    internal fun salvarDadosUsuario() {
        val usuarioMap = hashMapOf(
            "nome" to nome,
            "cpf" to cpf,
            "email" to email,
            "telefone" to telefone
        )
        //TODO Melhorar futuramente, talvez jogar um throw dizendo que ocorreu erro ao salvar no banco as informações
        //e falar para conferir as informações de login e tentar novamente, algo do tipo.
        db.collection("Usuarios")
            .document(nome).set(usuarioMap).addOnSuccessListener {
                Log.d("Sucesso", "salvarDadosUsuario() - Usuário cadastrado com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("Erro", "salvarDadosUsuario() - Erro ao cadastrar usuário", e)
            }
    }

    fun recuperarDadosUsuarios() {
        val usuariosArray = ArrayList<Usuario>()
        db.collection("Usuarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    usuariosArray.add(
                        Usuario(
                            document.data["nome"].toString(),
                            document.data["cpf"].toString(),
                            document.data["email"].toString(),
                            document.data["telefone"].toString()
                        )
                    )
                }
                Log.d("Sucesso", "recuperarUsuarios() - $usuariosArray")
            }
    }

    //TODO Validar esse retorno, se faz sentido para carregar informações na tela depois de login
    fun recuperarDadosUsuario(id: String): Usuario {
        lateinit var usuario: Usuario
        db.collection("Usuarios").document(id)
            .addSnapshotListener { documento, error ->
                if (documento != null) {
                    val nome = documento.getString("nome")
                    val cpf = documento.getString("cpf")
                    val email = documento.getString("email")
                    val telefone = documento.getString("telefone")
                    usuario = Usuario(nome!!, cpf!!, email!!, telefone!!)
                }
            }
        return usuario
    }
}
