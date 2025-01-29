package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
import javax.inject.Inject

class RegisterPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    RegisterPasswordUseCase {
    override suspend fun invoke(user: RegisterModelRequest): RegisterResponseOk {
        return try{userRepository.registerPassword(user)}
        catch (e: Exception) { throw Exception(e.message.toString()) }
    }
}