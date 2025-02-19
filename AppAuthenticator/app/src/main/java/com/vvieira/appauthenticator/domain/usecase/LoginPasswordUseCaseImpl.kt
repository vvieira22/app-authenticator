package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.UserRepository
import com.vvieira.appauthenticator.domain.model.ApiException
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.ResultRequest
import javax.inject.Inject

class LoginPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    LoginPasswordUseCase {
    override suspend fun invoke(user: LoginModelRequest): ResultRequest {
        return try { userRepository.loginPassword(user) }
        catch (e: ApiException) { throw ApiException(e.responseMessage, e.errorCode) }
        catch(e: Exception) { throw Exception(e.message) }
    }
}