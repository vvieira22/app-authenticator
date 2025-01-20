package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk

interface LoginPasswordUseCase {
    suspend operator fun invoke(user: LoginModelRequest): LoginResponseOk?
}