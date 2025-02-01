package com.vvieira.appauthenticator.ui

import Event
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCase
import com.vvieira.appauthenticator.domain.usecase.registerUserUseCase
import com.vvieira.appauthenticator.util.BIOMETRIC_AUTH
import com.vvieira.appauthenticator.util.DEFAUT_AUTH
import com.vvieira.appauthenticator.util.FACEBOOK_AUTH
import com.vvieira.appauthenticator.util.GOOGLE_AUTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticViewModel @Inject constructor(
    private val loginPassword: LoginPasswordUseCase,
    private val registerUser: registerUserUseCase
) : ViewModel() {

    private var isFormValid = false

    //TESTE https://developer.android.com/topic/architecture/ui-layer/events#views_1
    //TODO VALIDAR SE TEM COMO CONVERTER TODOS ESTADOS PARA MutableStateFlow
    data class LatestNewsUiState(
        val isLoading: Boolean = false,
    )
    data class LoginUiState(
        val teste: String = ""
    )
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    //LOGIN
    private val _userFieldMutableData = MutableLiveData<String?>()
    val userField: LiveData<String?> = _userFieldMutableData

    private val _passwordFieldMutableData = MutableLiveData<String?>()
    val passwordField: LiveData<String?> = _passwordFieldMutableData

    //tem que ser evento, pra n chamar mais de uma vez se eu for pro cadastro e depois voltar.
    private val _loginResponseMutableData = MutableLiveData<Event<String>>()
    val loginResponse: LiveData<Event<String>> = _loginResponseMutableData

    //REGISTER - PASSWORD - GOOGLE - FACEBOOK
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
//            _uiState.value = LoginUiState("teste")
            try {
                val response = loginPassword(LoginModelRequest(usuario, senha))
                _loginResponseMutableData.value = Event(response?.token ?: "")
            } catch (e: Exception) {
                Log.d("Erro AuthenticViewModel: ", e.message.toString())
                _loginResponseMutableData.value = Event("")
            }
        }
    }

    fun registerPass(usuario: RegisterModelRequest, type: String) = viewModelScope.launch {
        isFormValid = true

        if (type == DEFAUT_AUTH) {
            _nameFieldMutableData.value = fieldStringError(usuario.name)
            _documentFieldMutableData.value = fieldStringError(usuario.document)
            _emailFieldMutableData.value = fieldStringError(usuario.email)
            _phoneFieldMutableData.value = fieldStringError(usuario.telefone)
            _passwordRegisterFieldMutableData.value = fieldStringError(usuario.password)
//            _dataNascimentoFieldMutableData.value = fieldStringError(usuario.data_nascimento) //TODO
        } else if (type == GOOGLE_AUTH) {
            _gmailIdRegisterFieldMutableData.value = fieldStringError(usuario.gmail_id)
        } else if (type == FACEBOOK_AUTH) {
            _facebookIdRegisterFieldMutableData.value = fieldStringError(usuario.facebook_id)
        } else if (type == BIOMETRIC_AUTH) {
        } else {
            throw Exception("Tipo de login não encontrado")
        }

        if (isFormValid) {
            try {
                val response = registerUser(usuario, type)
                _registerResponseMutableData.value = response.toString()
            } catch (e: Exception) {
                Log.d("Erro AuthenticViewModel: ", e.message.toString())
                _registerResponseMutableData.value = ""
            }
        }
    }

    fun registerWithGoogle(usuario: RegisterModelRequest, type: String) = viewModelScope.launch {
        isFormValid = true
        _emailFieldMutableData.value = fieldStringError(usuario.email)
        _gmailIdRegisterFieldMutableData.value = fieldStringError(usuario.gmail_id)

        if (isFormValid) {
            try {
                val response = registerUser(usuario, type)
                _registerResponseMutableData.value = response.toString()
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

    //TODO USAR ISSO PARA LIMPAR, N SEI PQ A PORCARIA DO LIVE DATA N TA LIMPANDO.
    fun userMessageShown() {
        _uiState.update { test ->
            test.copy(teste = "")
        }
    }
}
