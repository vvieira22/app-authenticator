package com.vvieira.appauthenticator.ui

import Event
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.domain.model.ApiException
import com.vvieira.appauthenticator.domain.model.HttpCodeAndMesage
import com.vvieira.appauthenticator.domain.model.Login
import com.vvieira.appauthenticator.domain.model.LoginFormState
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.Register
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.usecase.CheckSocialAuthenticUseCase
import com.vvieira.appauthenticator.domain.usecase.LoginPasswordUseCase
import com.vvieira.appauthenticator.domain.usecase.RegisterUserUseCase
import com.vvieira.appauthenticator.util.BIRTHDAY
import com.vvieira.appauthenticator.util.DEFAUT_AUTH
import com.vvieira.appauthenticator.util.DOCUMENT
import com.vvieira.appauthenticator.util.EMAIL
import com.vvieira.appauthenticator.util.FACEBOOK_AUTH
import com.vvieira.appauthenticator.util.FACEBOOK_ID
import com.vvieira.appauthenticator.util.GMAIL_ID
import com.vvieira.appauthenticator.util.GOOGLE_AUTH
import com.vvieira.appauthenticator.util.NAME
import com.vvieira.appauthenticator.util.PASSWORD
import com.vvieira.appauthenticator.util.PHONE
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_DEFAULT_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_FACEBOOK_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_GOOGLE_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.NOT_REGISTERED_YET
import com.vvieira.appauthenticator.util.SpecificMsgTypes.AUTH_SOCIAL_ERRORS
import com.vvieira.appauthenticator.util.Utils
import com.vvieira.appauthenticator.util.Utils.Companion.getMessageFromResponse
import com.vvieira.appauthenticator.util.Utils.Companion.isCpf
import com.vvieira.appauthenticator.util.Utils.Companion.isValidCNPJ
import com.vvieira.appauthenticator.util.Utils.Companion.isValidCPF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticViewModel @Inject constructor(
    private val loginPassword: LoginPasswordUseCase,
    private val registerUser: RegisterUserUseCase,
    private val checkSocialAuthentic: CheckSocialAuthenticUseCase
) : ViewModel() {
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxTESTE INTERNO WELCOME SCREENxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//
    private val _dataLoginWelcome = MutableLiveData<String>()
    val dataLoginWelcome: LiveData<String> get() = _dataLoginWelcome
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//

    private var isFormValid = false

    // StateFlow: + moderno e mais interessante que usar LiveData :)
    //https://developer.android.com/topic/architecture/ui-layer/events#views_1

    //-----------------------------------------LOGIN----------------------------------------------//
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState

    //Tem que ser evento, pra n chamar mais de uma vez se eu for pro cadastro e depois voltar p.exp.
    private val _loginResult = MutableLiveData<Event<HttpCodeAndMesage>>()
    val loginResponse: LiveData<Event<HttpCodeAndMesage>> = _loginResult

    //FIELDS
    private val _emailLoginFieldMutableData = MutableLiveData<String?>()
    val emaiLoginField: LiveData<String?> = _emailLoginFieldMutableData

    private val _passwordLoginFieldMutableData = MutableLiveData<String?>()
    val passwordLoginField: LiveData<String?> = _passwordLoginFieldMutableData
    //--------------------------------------------------------------------------------------------//


    //=========================================REGISTER===========================================//
    private val _registerFormState = MutableStateFlow(LoginFormState())
    val registerFormState: StateFlow<LoginFormState> = _registerFormState

    //Tem que ser evento, pra n chamar mais de uma vez se eu for pro cadastro e depois voltar p.exp.
    private val _registerResult = MutableLiveData<Event<HttpCodeAndMesage>>()
    val registerResponse: LiveData<Event<HttpCodeAndMesage>> = _registerResult

    //FIELDS
    private val _emailRegisterFieldMutableData = MutableLiveData<String?>()
    val emailRegisterField: LiveData<String?> = _emailRegisterFieldMutableData

    private val _passwordRegisterFieldMutableData = MutableLiveData<String?>()
    val passwordRegisterField: LiveData<String?> = _passwordRegisterFieldMutableData

    private val _nameFieldMutableData = MutableLiveData<String?>()
    val nameField: LiveData<String?> = _nameFieldMutableData

    private val _documentFieldMutableData = MutableLiveData<String?>()
    val documentField: LiveData<String?> = _documentFieldMutableData

    private val _dataNascimentoFieldMutableData = MutableLiveData<String?>()
    val dataNascimentoField: LiveData<String?> = _dataNascimentoFieldMutableData

    private val _phoneFieldMutableData = MutableLiveData<String?>()
    val phoneField: LiveData<String?> = _phoneFieldMutableData
    //============================================================================================//


    //MAYBE MUTUAL DATA, REGISTER + LOGIN = SOCIAL AUTHENTICATION
    private val _facebookIdRegisterFieldMutableData = MutableLiveData<String?>()
    val facebookIdRegisterField: LiveData<String?> = _facebookIdRegisterFieldMutableData

    private val _gmailIdRegisterFieldMutableData = MutableLiveData<String?>()
    val gmailIdRegisterField: LiveData<String?> = _gmailIdRegisterFieldMutableData

    private val _biometricDataRegisterFieldMutableData = MutableLiveData<String?>()
    val biometricDataRegisterField: LiveData<String?> = _biometricDataRegisterFieldMutableData


    /////////////////////////////////////////SOCIAL LOGIN///////////////////////////////////////////
    private val _socialFormState = MutableStateFlow(LoginFormState())
    val socialFormState: StateFlow<LoginFormState> = _socialFormState

    private val _socialAuthMutableData = MutableLiveData<Register>()
    val socialAuth: LiveData<Register> = _socialAuthMutableData

    private val _socialResult = MutableLiveData<Event<String>>()
    val socialResult: LiveData<Event<String>> = _socialResult

    private val _socialAuthInformations = MutableLiveData<Register>()
    val socialAuthInformations: LiveData<Register> = _socialAuthInformations
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //TODO: AUTHENTICATE SOCIAL -> IF HAS REGISTERED WITH SOCIAL SELECTED BTN -> SIGN IN, ISN'T? -> GO TO REGISTER SCREEN.
    fun socialAuth(user: Register, context: Context) = viewModelScope.launch {
        isFormValid = true
        val type = user.type.toString()
        val email = user.email.toString()
        val password = user.password.toString()
        val name = user.name.toString()
        var id_token = ""
        var result = ""

        if (type == GOOGLE_AUTH) {
            id_token = user.gmailId.toString()
        } else if (type == FACEBOOK_AUTH) {
            id_token = user.facebookId.toString()
        } else {
            isFormValid = false
        }

        if (isFormValid) {
            try {
                //Start of loading
                _socialFormState.value = _socialFormState.value.copy(
                    isLoading = true,
                    error = null
                )
                val userModel = LoginModelRequest(
                    email = email, password = "", id_token = id_token, biometric_data = ""
                )
                val response = checkSocialAuthentic(userModel, type)
                var resultRaw = (response?.okResponse?.message) //e.g: {"detail":"User not registered yet."}
                if (resultRaw != null) {
                    result = getMessageFromResponse(
                        response = resultRaw,
                        context = context,
                        specificMsg = AUTH_SOCIAL_ERRORS
                    )
                }
                when {
                    (resultRaw == NOT_REGISTERED_YET) -> {
                        _socialAuthInformations.value = user
                    }

                    (resultRaw == ALREADY_GOOGLE_REGISTERED && type == GOOGLE_AUTH) -> {
                        try {

                        } catch (e: Exception) {
                        }
                    }

                    (resultRaw == ALREADY_FACEBOOK_REGISTERED && type == FACEBOOK_AUTH) -> {
                        //TODO DO LOGIN
                    }

                    (resultRaw == ALREADY_DEFAULT_REGISTERED) -> {
                        _socialResult.value = Event(result)
                        //TODO PERGUNTAR SE DESEJA VINCULAR A CONTA A REDE SOCIAL PARA FAZER LOGIN.
                        //TODO VER SE E SECURO FAZER ESSA PERGUNTA, OU MANDAR ELE ENTRAR COM EMAIL E SENHA E ELE APERTAR EM VINCULAR
                    }
//                    else -> {_socialFormState.value = _socialFormState.value.copy(isLoading = false, error = null)}
                }
                //TODO VALIDAR PQ ELE TA CAINDO AQUI QUANDO
            } catch (e: Exception) {
                var resultRaw = (e.message)
                if (resultRaw == NOT_REGISTERED_YET) {
                    _socialAuthInformations.value = user
                } else {
                    val errorMsg = getMessageFromResponse(e.message.toString(), context)
                    _socialFormState.value =
                        _socialFormState.value.copy(isLoading = false, error = e.message.toString())
                    _socialResult.value = Event(errorMsg)
                }
            }
        }
    }

    fun loginPassword(login: Login, context: Context) = viewModelScope.launch {
        isFormValid = true
        val type = login.type.toString()
        var email = login.email.toString()
        var password = login.password.toString()
        var facebookId = login.facebookId.toString()
        var gmailId = login.gmailId.toString()
        var biometricData = login.biometricData.toString()

        if (type == DEFAUT_AUTH) {
            email = login.email.toString()
            password = login.password.toString()

            _emailLoginFieldMutableData.value = valEmail(context = context, value = email)
            _passwordLoginFieldMutableData.value = valPassword(context, password)

//        } else if (type == GOOGLE_AUTH) {
//            gmailId = login.gmailId.toString()
//            isFormValid = fieldStringError(gmailId)
//        } else if (type == FACEBOOK_AUTH) {
//            facebookId = login.facebookId.toString()
//            isFormValid = fieldStringError(facebookId)
        } else { //TODO, VER SE VALE A PENA RETORNAR ERRO AQUI JA, OU SO DAR FALSE E ELE CONTINUAR A EXECUCAO.
        }

        if (isFormValid) {
            _loginFormState.value =
                _loginFormState.value.copy(
                    isLoading = true,
                    error = null
                ) // Inicio Carregamento
            try {
                val response = loginPassword(LoginModelRequest(email, password))
                _loginFormState.value = _loginFormState.value.copy(isLoading = false, error = null)
                _dataLoginWelcome.value = email
                _loginResult.value = Event(
                    HttpCodeAndMesage(
                        response.MessageResponse.toString(),
                        response.statusCode!!
                    )
                )
            } catch (e: ApiException) {
                val errorMsg = getMessageFromResponse(e.errorCode.toString(), context)
                _loginFormState.value =
                    _loginFormState.value.copy(
                        isLoading = false,
                        error = e.message.toString()
                    )
                _loginResult.value = Event(HttpCodeAndMesage(errorMsg, e.errorCode))
            } catch (e: Exception) {
                val errorMsg = getMessageFromResponse(404.toString(), context)
                _loginFormState.value =
                    _loginFormState.value.copy(
                        isLoading = false,
                        error = e.message.toString()
                    )
                _loginResult.value = Event(HttpCodeAndMesage(errorMsg, 404))
            }

        }
//        _loginFormState.value = _loginFormState.value.copy(isLoading = false, error = null)
    }

    fun registerPass(user: Register, context: Context) = viewModelScope.launch {
        isFormValid = true

        val type = user.type
        val name = user.name.toString()
        val document = user.document.toString()
        val email = user.email.toString()
        val phone = user.phone.toString()
        val password = user.password.toString()
        val birthDate = user.birthDate.toString()
        val facebookId = user.facebookId.toString()
        val gmailId = user.gmailId.toString()

        if (type == DEFAUT_AUTH) {
            _emailRegisterFieldMutableData.value =
                valRegisterFields(context, field = EMAIL, value = email)
            _passwordRegisterFieldMutableData.value =
                valRegisterFields(context, field = PASSWORD, value = password)
            _nameFieldMutableData.value = valRegisterFields(context, field = NAME, value = name)
            _documentFieldMutableData.value =
                valRegisterFields(context, field = DOCUMENT, value = document)
            _phoneFieldMutableData.value =
                valRegisterFields(context, field = PHONE, value = phone)
//            _dataNascimentoFieldMutableData.value = fieldStringError(usuario.data_nascimento) //TODO

        } else if (type == GOOGLE_AUTH) {
            _gmailIdRegisterFieldMutableData.value =
                valRegisterFields(context, field = GMAIL_ID, value = gmailId)
//        } else if (type == FACEBOOK_AUTH) {
//            _facebookIdRegisterFieldMutableData.value = fieldStringError(usuario.facebook_id)
//        } else if (type == BIOMETRIC_AUTH) {
//        }
        } else {//TODO, VER SE VALE A PENA RETORNAR ERRO AQUI JA, OU SO DAR FALSE E ELE CONTINUAR A EXECUCAO. >>> TALVEZ NEM PRECISE DESSE ELSE, PQ QUEM CHAMA SEMPRE TEM UM TYPE !!!!
        }

        if (isFormValid) {
            _registerFormState.value = _registerFormState.value.copy(
                isLoading = true,
                error = null
            ) // Inicio Carregamento
            try {
                val userModel = RegisterModelRequest(
                    email = email, password = password, name = name,
                    document = document, data_nascimento = birthDate,
                    facebook_id = facebookId, gmail_id = gmailId, telefone = phone
                )
                val response = registerUser(userModel, type)
                _registerFormState.value =
                    registerFormState.value.copy(isLoading = false, error = null)
                _registerResult.value = Event(
                    HttpCodeAndMesage(
                        response.MessageResponse.toString(),
                        response.statusCode!!
                    )
                )
            } catch (e: ApiException) {
                val errorMsg = getMessageFromResponse(e.errorCode.toString(), context)
                _registerFormState.value =
                    _registerFormState.value.copy(
                        isLoading = false,
                        error = e.message.toString()
                    )
                _registerResult.value = Event(HttpCodeAndMesage(errorMsg, e.errorCode))
            }
            catch (e: Exception) {
                val errorMsg = getMessageFromResponse(404.toString(), context)
                _registerFormState.value =
                    _registerFormState.value.copy(
                        isLoading = false,
                        error = e.message.toString()
                    )
                _registerResult.value = Event(HttpCodeAndMesage(errorMsg, 404))
            }
        }
    }

//    fun registerWithGoogle(usuario: RegisterModelRequest, type: String) = viewModelScope.launch {
//        isFormValid = true
//        _emailFieldMutableData.value = fieldStringError(usuario.email)
//        _gmailIdRegisterFieldMutableData.value = fieldStringError(usuario.gmail_id)
//
//        if (isFormValid) {
//            try {
//                val response = registerUser(usuario, type)
//                _registerResponseMutableData.value = response.toString()
//            } catch (e: Exception) {
//                Log.d("Erro AuthenticViewModel: ", e.message.toString())
//                _registerResponseMutableData.value = ""
//            }
//        }
//    }

    private fun fieldStringError(context: Context, value: String): String? {
        return if (value.isEmpty()) {
            isFormValid = false
            context.getString(R.string.campos_vazios)
        } else null
    }

    private fun valPassword(context: Context, value: String): String? {
        return if (value.isEmpty()) {
            isFormValid = false
            context.getString(R.string.senha_vazio)
        } else if (!Utils.valPassword(value)) {
            isFormValid = false
            context.getString(R.string.password_weak)
        } else null
    }

    private fun valEmail(context: Context, value: String): String? {
        return if (value.isEmpty()) {
            isFormValid = false
            context.getString(R.string.email_vazio)
        } else if (!Utils.valEmail(value)) {
            isFormValid = false
            context.getString(R.string.email_invalido)
        } else null
    }

    private fun valRegisterFields(context: Context, field: String, value: String): String? {
        return when {
            (field == EMAIL) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.email_vazio)
                } else if (!Utils.valEmail(value)) {
                    isFormValid = false
                    return context.getString(R.string.password_weak)
                } else null
            }

            (field == PASSWORD) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.senha_vazio)
                } else if (!Utils.valPassword(value)) {
                    isFormValid = false
                    return context.getString(R.string.password_weak)
                } else null
            }

            (field == NAME) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.nome_vazio)
                } else null
            }

            (field == DOCUMENT) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.cpf_cnpj_vazio)
                } else if (value.isCpf()) {
                    if (!value.isValidCPF()) {
                        isFormValid = false
                        return context.getString(R.string.cpf_invalido)
                    }
                    null
                } else {
                    if (!value.isValidCNPJ()) {
                        isFormValid = false
                        return context.getString(R.string.cnpj_invalido)
                    }
                    null
                }
            }

            (field == PHONE) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.telefone_vazio)
                } else null
            } //TODO VALIDAR SE O TELEFONE
            (field == BIRTHDAY) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.campos_vazios)
                } else null
            } //TODO VALIDAR DATA DE ANIVERSARIO, TALVEZ DEIXAR DIGITAR EM VEZ DE PEGAR DO CALENDARIO
            (field == FACEBOOK_ID) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.campos_vazios)
                } else null
            }

            (field == GMAIL_ID) -> {
                if (value.isEmpty()) {
                    isFormValid = false
                    return context.getString(R.string.campos_vazios)
                } else null
            }

            else -> null
        }
    }
//
//    //TODO USAR ISSO PARA LIMPAR, N SEI PQ A PORCARIA DO LIVE DATA N TA LIMPANDO.
//    fun userMessageShown() {
//    _loginFormState.update { test ->
//            test.copy(null,null,null,null,null,null)
//        }
//    }
}
