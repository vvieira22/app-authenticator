package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest

interface LoginPasswordUseCase {
    suspend operator fun invoke(user: LoginModelRequest): ResultRequest
}