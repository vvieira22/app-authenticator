package com.vvieira.appauthenticator.data

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
import com.vvieira.appauthenticator.domain.model.ResultRequest
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val loginDataSource: LoginDataSource
) {
    suspend fun login(user: LoginModelRequest, type: String): ResultRequest =
        loginDataSource.login(user, type)

    suspend fun registerUser(user: RegisterModelRequest, type: String): ResultRequest =
        loginDataSource.registerUser(user, type)

    suspend fun checkSocialAuthentic(user: LoginModelRequest, type: String): ResultRequest =
        loginDataSource.checkSocialAuthentic(user, type)
}