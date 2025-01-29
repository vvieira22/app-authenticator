package com.vvieira.appauthenticator.data

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk

interface LoginDataSource {
    suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk
    suspend fun registerPassword(register: RegisterModelRequest): RegisterResponseOk
}