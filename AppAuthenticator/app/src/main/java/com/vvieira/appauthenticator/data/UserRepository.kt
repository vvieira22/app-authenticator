package com.vvieira.appauthenticator.data
import javax.inject.Inject
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk

class UserRepository @Inject constructor(
    private val loginDataSource: LoginDataSource
) {
    suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk =
        loginDataSource.loginPassword(user)

    suspend fun registerPassword(user: RegisterModel): String =
        loginDataSource.registerPassword(user)
}