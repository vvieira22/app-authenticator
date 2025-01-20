package com.vvieira.appauthenticator.data.di

import com.vvieira.appauthenticator.data.DataBaseDataSource
import com.vvieira.appauthenticator.data.LoginDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)//sabemos que nosso datasource vai ser usado em todos casos de uso, viver em toda nossa aplicacao
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindLoginDataSource(loginDataSource: DataBaseDataSource): LoginDataSource
}