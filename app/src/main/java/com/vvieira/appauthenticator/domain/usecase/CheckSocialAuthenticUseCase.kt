package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest

interface CheckSocialAuthenticUseCase {
    suspend operator fun invoke(user: LoginModelRequest, type: String): ResultRequest?
}