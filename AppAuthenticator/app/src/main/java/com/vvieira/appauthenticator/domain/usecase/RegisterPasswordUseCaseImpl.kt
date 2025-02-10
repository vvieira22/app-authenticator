package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.ApiException
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    RegisterUserUseCase {
    override suspend fun invoke(user: RegisterModelRequest, type: String): ResultRequest {
        return try {
            userRepository.registerUser(user, type)
        } catch (e: ApiException) {
            throw ApiException(e.responseMessage, e.errorCode)
        }
    }
}