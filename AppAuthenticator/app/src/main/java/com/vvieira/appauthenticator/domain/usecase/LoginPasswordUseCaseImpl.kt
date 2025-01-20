package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import javax.inject.Inject

class LoginPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    LoginPasswordUseCase {
    override suspend fun invoke(user: LoginModelRequest): LoginResponseOk {
        return try{ userRepository.loginPassword(user) }
        catch (e: Exception) { throw Exception(e.message.toString()) }
    }
}