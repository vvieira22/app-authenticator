package com.vvieira.appauthenticator.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCase
import com.vvieira.appauthenticator.domain.usecase.RegisterPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticViewModel @Inject constructor(
    private val loginPassword: LoginPasswordUseCase,
    private val registerPassword: RegisterPasswordUseCase
) : ViewModel() {

    private var isFormValid = false

    //LOGIN
    private val _userFieldMutableData = MutableLiveData<String?>()
    val userField: LiveData<String?> = _userFieldMutableData

    private val _passwordFieldMutableData = MutableLiveData<String?>()
    val passwordField: LiveData<String?> = _passwordFieldMutableData

    private val _loginResponseMutableData = MutableLiveData<String?>()
    val loginResponse: LiveData<String?> = _loginResponseMutableData

    //REGISTER
    private val _emailFieldMutableData = MutableLiveData<String?>()
    val emailField: LiveData<String?> = _emailFieldMutableData

    private val _passwordRegisterFieldMutableData = MutableLiveData<String?>()
    val passwordRegisterField: LiveData<String?> = _passwordRegisterFieldMutableData

    private val _facebookIdRegisterFieldMutableData = MutableLiveData<String?>()
    val facebookIdRegisterField: LiveData<String?> = _facebookIdRegisterFieldMutableData

    private val _gmailIdRegisterFieldMutableData = MutableLiveData<String?>()
    val gmailIdRegisterField: LiveData<String?> = _gmailIdRegisterFieldMutableData

    private val _biometricDataRegisterFieldMutableData = MutableLiveData<String?>()
    val biometricDataRegisterField: LiveData<String?> = _biometricDataRegisterFieldMutableData

    private val _nameFieldMutableData = MutableLiveData<String?>()
    val nameField: LiveData<String?> = _nameFieldMutableData

    private val _documentFieldMutableData = MutableLiveData<String?>()
    val documentField: LiveData<String?> = _documentFieldMutableData

    private val _dataNascimentoFieldMutableData = MutableLiveData<String?>()
    val dataNascimentoField: LiveData<String?> = _dataNascimentoFieldMutableData

    private val _phoneFieldMutableData = MutableLiveData<String?>()
    val phoneField: LiveData<String?> = _phoneFieldMutableData

    private val _registerResponseMutableData = MutableLiveData<String?>()
    val registerResponse: LiveData<String?> = _registerResponseMutableData

    fun loginPassword(usuario: String, senha: String) = viewModelScope.launch {
        isFormValid = true

        _userFieldMutableData.value = fieldStringError(usuario)
        _passwordFieldMutableData.value = fieldStringError(senha)

        if (isFormValid) {
            try {
                val response = loginPassword(LoginModelRequest(usuario, senha))
                _loginResponseMutableData.value = response?.token
            } catch (e: Exception) {
                Log.d("Erro AuthenticViewModel: ", e.message.toString())
                _loginResponseMutableData.value = ""
            }
        }
    }

    fun registerPass(usuario: RegisterModelRequest) = viewModelScope.launch {
        isFormValid = true
//        _emailFieldMutableData.value = fieldStringError(usuario.email)
//        _passwordRegisterFieldMutableData.value = fieldStringError(usuario.password)
//        _facebookIdRegisterFieldMutableData.value = fieldStringError(usuario.facebook_id)
//        _gmailIdRegisterFieldMutableData.value = fieldStringError(usuario.gmail_id)
//        _biometricDataRegisterFieldMutableData.value = fieldStringError(usuario.biometric_data)
//        _nameFieldMutableData.value = fieldStringError(usuario.name)
//        _documentFieldMutableData.value = fieldStringError(usuario.document)
//        _dataNascimentoFieldMutableData.value = fieldStringError(usuario.data_nascimento)
//        _phoneFieldMutableData.value = fieldStringError(usuario.telefone)

        if (isFormValid) {
            try {
                val response = registerPassword(usuario)
                _registerResponseMutableData.value = response?.toString()
            } catch (e: Exception) {
                Log.d("Erro AuthenticViewModel: ", e.message.toString())
                _registerResponseMutableData.value = ""
            }
        }
    }

    private fun fieldStringError(value: String): String? {
        return if (value.isEmpty()) {
            isFormValid = false
            "Campo Vazio !"
        } else null
    }

}
