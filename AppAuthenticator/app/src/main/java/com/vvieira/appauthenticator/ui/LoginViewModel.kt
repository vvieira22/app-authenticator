package com.vvieira.appauthenticator.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginPassword: LoginPasswordUseCase
) : ViewModel() {

    private var isFormValid = false

    private val _userFieldMutableData = MutableLiveData<String?>()
    val userField: LiveData<String?> = _userFieldMutableData

    private val _passwordFieldMutableData = MutableLiveData<String?>()
    val passwordField: LiveData<String?> = _passwordFieldMutableData

    private val _loginResponseMutableData = MutableLiveData<String?>()
    val loginResponse: LiveData<String?> = _loginResponseMutableData

    fun loginPassword(usuario: String, senha: String) = viewModelScope.launch {
        isFormValid = true

        _userFieldMutableData.value = fieldStringError(usuario)
        _passwordFieldMutableData.value = fieldStringError(senha)

        if (isFormValid) {
            try {
                val response = loginPassword(LoginModelRequest(usuario, senha))
                _loginResponseMutableData.value = response?.token
            } catch (e: Exception) {
                Log.d("Erro LoginViewModel: ", e.message.toString())
                _loginResponseMutableData.value = ""
            }
        }
    }

    private fun fieldStringError(value: String): String? {
        return if (value.isEmpty()) {
            isFormValid = false
            "campo vazio"
        } else null
    }

}
