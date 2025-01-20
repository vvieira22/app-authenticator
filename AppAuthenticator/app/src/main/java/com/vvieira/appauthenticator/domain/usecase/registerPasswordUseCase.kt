package com.vvieira.appauthenticator.domain.usecase
import com.vvieira.appauthenticator.data.RegisterModel

interface registerPasswordUseCase {
    suspend operator fun invoke(user: RegisterModel): String
}