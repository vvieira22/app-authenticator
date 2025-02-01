package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
import javax.inject.Inject

class registerUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    registerUserUseCase {
    override suspend fun invoke(user: RegisterModelRequest, type: String): RegisterResponseOk {
        return try{userRepository.registerUser(user, type)}
        catch (e: Exception) { throw Exception(e.message.toString()) }
    }
}