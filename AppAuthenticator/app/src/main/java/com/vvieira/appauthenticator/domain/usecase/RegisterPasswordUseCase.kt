package com.vvieira.appauthenticator.domain.usecase
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk

interface RegisterPasswordUseCase {
    suspend operator fun invoke(user: RegisterModelRequest): RegisterResponseOk
}