package com.vvieira.appauthenticator.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Login(
    val email: String? = null,
    val password: String? = null,
    val facebookId: String? = null,
    val googleId: String? = null,
    val biometricData: String? = null,
    val type: String
)

@Parcelize
data class Register(
    val email: String? = null,
    val password: String? = null,
    val name: String? = null,
    val document: String? = null,
    val phone: String? = null,
    val birthDate: String? = null,
    val facebookId: String? = null,
    val googleId: String? = null,
    val biometricData: String? = null,
    val creationDate: String? = null,
    val type: String
): Parcelable

data class LoginFormState(
    val isLoading: Boolean = false,
    val error: String? = null
)