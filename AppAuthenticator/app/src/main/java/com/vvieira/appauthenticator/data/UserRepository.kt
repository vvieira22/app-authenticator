package com.vvieira.appauthenticator.data
import javax.inject.Inject
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk

class UserRepository @Inject constructor(
    private val loginDataSource: LoginDataSource
) {
    suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk =
        loginDataSource.loginPassword(user)

    suspend fun registerUser(user: RegisterModelRequest, type: String): RegisterResponseOk =
        loginDataSource.registerUser(user, type)
}