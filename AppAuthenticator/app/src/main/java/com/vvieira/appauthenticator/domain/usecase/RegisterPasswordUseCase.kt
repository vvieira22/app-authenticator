package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest

interface RegisterUserUseCase {
    suspend operator fun invoke(user: RegisterModelRequest, type: String): ResultRequest
}