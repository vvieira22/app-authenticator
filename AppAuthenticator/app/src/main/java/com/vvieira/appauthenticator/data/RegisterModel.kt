package com.vvieira.appauthenticator.data

data class RegisterModel (
    val nome: String,
    val cpfOrCnpj: String,
    val email: String,
    val telefone: String,
    val senha: String,
)