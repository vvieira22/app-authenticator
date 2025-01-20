package com.vvieira.appauthenticator.domain.model

import com.google.gson.annotations.SerializedName

data class LoginModelRequest (
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("id_token") //maybe facebook or google login.
    val facebook_id: String = "",

    @SerializedName("biometric_data")
    val biometric_data: String = ""
)

data class LoginResponseOk(
    @SerializedName("access_token")
    val token: String
)

data class LoginResponseError(
    @SerializedName("detail")
    val error: String
)