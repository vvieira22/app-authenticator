package com.vvieira.appauthenticator.data

import AuthEndPoint
import android.util.Log
import com.google.gson.Gson
import com.vvieira.appauthenticator.domain.model.ApiException
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.OkResponse
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
import com.vvieira.appauthenticator.domain.model.ResultRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataBaseDataSource @Inject constructor(
    clientRetrofit: Retrofit
) : LoginDataSource {
    private val clientRetrofitWithRoutes = clientRetrofit.create(AuthEndPoint::class.java)
    override suspend fun loginPassword(user: LoginModelRequest): ResultRequest {
        return suspendCoroutine { continuation ->
            var responseLoginPassword = ""
            clientRetrofitWithRoutes.login("password", user).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body()?.string()
                            val loginResponse: OkResponse =
                                Gson().fromJson(jsonResponse, OkResponse::class.java)
                            val request = ResultRequest(response.code(), loginResponse)
                            continuation.resumeWith(Result.success(request))
                        } else {
                            val errCode = response.code()
                            val responseMessage = response.message()
                            val errBody = response.errorBody()?.string()
                            val checkResponse: OkResponse = Gson().fromJson(errBody, OkResponse::class.java)
                            Log.d("RESPOSTA-ELSE", "jsonResponse: $responseMessage")
                            Log.i("registerUser-ELSE", "errBody:  $errBody")
                            Log.d("registerUser-ELSE", "jsonResponse: $checkResponse")
                            continuation.resumeWithException((ApiException(responseMessage, errCode)))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("RESPOSTA-FAILURE", "jsonResponse: ${t.message}")
                        responseLoginPassword = t.message.toString()
                        continuation.resumeWith(Result.failure(Exception(responseLoginPassword)))
                    }
                })
        }
    }

    override suspend fun registerUser(
        register: RegisterModelRequest,
        type: String
    ): ResultRequest {
        return suspendCoroutine { continuation ->
            var responseregisterUser = ""
            clientRetrofitWithRoutes.register(type, register).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
//                            Log.i("RESPOSTA-OK", "${response.code()}")
//                            val jsonResponse = response.body()?.string() ?: ""
//                            Log.i("RESPOSTA-OK", "jsonResponse: $jsonResponse")
//                            val registerResponse: RegisterResponseOk =
//                                Gson().fromJson(jsonResponse, RegisterResponseOk::class.java)
//                            continuation.resumeWith(Result.success(registerResponse))

                            val jsonResponse = response.body()?.string()
                            val checkResponse: OkResponse =
                                Gson().fromJson(jsonResponse, OkResponse::class.java)
                            val request = ResultRequest(response.code(), checkResponse)

                            continuation.resumeWith(Result.success(request))

                        } else {
                            val errCode = response.code()
                            val responseMessage = response.message()
                            val errBody = response.errorBody()?.string()
                            val checkResponse: OkResponse = Gson().fromJson(errBody, OkResponse::class.java)
                            Log.i("registerUser-ELSE", "errBody:  $errBody")
                            Log.d("registerUser-ELSE", "jsonResponse: $checkResponse")
                            continuation.resumeWithException((ApiException(responseMessage, errCode)))
                        }
                    }


                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("RESPOSTA-FAILURE", "jsonResponse: ${t.message}")
                        responseregisterUser = t.message.toString()
                        continuation.resumeWith(Result.failure(Exception(responseregisterUser)))
                    }
                })
        }
    }

    override suspend fun checkSocialAuthentic(user: LoginModelRequest, type: String): ResultRequest {
        return suspendCoroutine  { continuation ->
            var responseOk = ""
            clientRetrofitWithRoutes.checkSocialAuthentic(user).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body()?.string()
                            val checkResponse: OkResponse =
                                Gson().fromJson(jsonResponse, OkResponse::class.java)
                            val request = ResultRequest(response.code(), checkResponse)

                            continuation.resumeWith(Result.success(request))
                        } else {
                            val errBody = response.errorBody()?.string()
                            val checkResponse: OkResponse = Gson().fromJson(errBody, OkResponse::class.java)

                            //TODO ESSA É UMA TENTATIVA DE RETORNAR CODIGO + BODY NO ERRO, ONDE SO DA PRA PASSAR STRING
                            //ATE DARIA PRA RETORNAR O OBJETO COMPLETO DO ERRO, MAS DA MUITO TRABALHO E TALVEZ NAO FACA SENTIDO.
//                            val request = ResultRequest(response.code(), checkResponse)
//                            val request = ResultRequest(MessageResponse = errBody)
                            continuation.resumeWithException(Exception(checkResponse.message))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("RESPOSTA-FAILURE", "jsonResponse: ${t.message}")
                        responseOk = t.message.toString()
                        continuation.resumeWith(Result.failure(Exception(responseOk)))
                    }
                })
        }
    }
}
