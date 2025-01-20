package com.vvieira.appauthenticator.domain.usecase

import com.vvieira.appauthenticator.data.RegisterModel
import com.vvieira.appauthenticator.data.UserRepository
import javax.inject.Inject

class registerPasswordUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) :
    registerPasswordUseCase {
    override suspend fun invoke(user: RegisterModel): String {
        return userRepository.registerPassword(user)

    }
}