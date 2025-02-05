package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest
import javax.inject.Inject

class CheckSocialAuthenticUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    CheckSocialAuthenticUseCase {
    override suspend fun invoke(user: LoginModelRequest, type: String): ResultRequest {
        return try {
            userRepository.checkSocialAuthentic(user, type)
        } catch (e: Exception) {
            val exception = e
            throw Exception(e.message.toString())
        }
    }
}