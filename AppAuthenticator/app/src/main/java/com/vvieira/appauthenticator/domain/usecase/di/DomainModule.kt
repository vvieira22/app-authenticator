package com.vvieira.appauthenticator.domain.usecase.di

import com.vvieira.appauthenticator.domain.usecase.CheckSocialAuthenticUseCase
import com.vvieira.appauthenticator.domain.usecase.CheckSocialAuthenticUseCaseImpl
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCase
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCaseImpl
import com.vvieira.appauthenticator.domain.usecase.RegisterUserUseCase
import com.vvieira.appauthenticator.domain.usecase.RegisterUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class) //vai ficar vivo ate a viewmodel morrer
interface DomainModule {
    @Binds
    fun bindLoginPasswordUseCase(useCase: LoginPasswordUseCaseImpl): LoginPasswordUseCase
    //quando alguem precisar da dependencia loginPasswordUseCase forneça loginPasswordUseCaseImpl
    //Se no futuro voce criar um nova implementacao para essa interface, so abrir o modulo do dagger e trocar a implementacao.
    //Desde que ela implemente a mesma interface.

    @Binds
    fun bindRegisterUserUseCase(useCase: RegisterUserUseCaseImpl): RegisterUserUseCase

    @Binds
    fun bindCheckSocialAuthenticUseCase(useCase: CheckSocialAuthenticUseCaseImpl): CheckSocialAuthenticUseCase
}