package com.vvieira.appauthenticator.data

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
import com.vvieira.appauthenticator.domain.model.ResultRequest

interface LoginDataSource {
    suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk
    suspend fun registerUser(register: RegisterModelRequest, type: String): RegisterResponseOk
    suspend fun checkSocialAuthentic(user: LoginModelRequest, type: String): ResultRequest
}