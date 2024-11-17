package com.vvieira.appauthenticator

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class Usuario(val nome : String, val cpf : String, val email : String, val telefone : String) {
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

}
