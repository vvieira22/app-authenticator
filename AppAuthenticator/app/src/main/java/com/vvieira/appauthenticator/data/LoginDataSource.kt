package com.vvieira.appauthenticator.data
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk

interface LoginDataSource {
    suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk
    suspend fun registerPassword(register: RegisterModel): String
}