package com.vvieira.appauthenticator.data

import LoginEndpoint
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
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
    val clientRetrofitWithRoutes = clientRetrofit.create(LoginEndpoint::class.java)
    override suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk {
        return suspendCoroutine { continuation ->
            var responseLoginPassword = ""
            Log.d("RESPOSTA-USER", "jsonResponse: $user")
            clientRetrofitWithRoutes.login("password", user).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body()?.string() ?: ""
                            val loginResponse: LoginResponseOk = Gson().fromJson(jsonResponse, LoginResponseOk::class.java)
                            continuation.resumeWith(Result.success(loginResponse))
                        }
                        else {
                            val errMsg = response.message()
                            Log.d("RESPOSTA-ELSE", "jsonResponse: $errMsg")
                            continuation.resumeWithException((Exception(errMsg)))
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

    override suspend fun registerPassword(register: RegisterModel): String {
        return suspendCoroutine { continuation ->
            val usuarioJson: JsonObject = Gson().toJsonTree(register).asJsonObject
            var responseRegisterPassword = ""

            clientRetrofitWithRoutes.register("password", usuarioJson).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body()
                            responseRegisterPassword = jsonResponse.toString()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        responseRegisterPassword = t.message.toString()
                    }
                })
            if (responseRegisterPassword != "") {
                continuation.resumeWith(Result.success(responseRegisterPassword))
            } else {
                continuation.resumeWith(Result.failure(Exception("Erro ao fazer login")))
            }
        }
    }

}