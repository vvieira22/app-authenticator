package com.vvieira.appauthenticator.domain.model

data class Login(
    val email: String? = null,
    val password: String? = null,
    val facebookId: String? = null,
    val gmailId: String? = null,
    val biometricData: String? = null,
    val type: String
)
data class Register(
    val email: String? = null,
    val password: String? = null,
    val name: String? = null,
    val document: String? = null,
    val phone: String? = null,
    val birthDate: String? = null,
    val facebookId: String? = null,
    val gmailId: String? = null,
    val biometricData: String? = null,
    val creationDate: String? = null,
    val type: String
)

data class LoginFormState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class AuthFormState(
    val isLoading: Boolean = false,
    val error: String? = null
)