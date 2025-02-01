package com.vvieira.appauthenticator.data

import AuthEndPoint
import android.util.Log
import com.google.gson.Gson
import com.vvieira.appauthenticator.domain.model.LoginModelRequest
import com.vvieira.appauthenticator.domain.model.LoginResponseOk
import com.vvieira.appauthenticator.domain.model.RegisterModelRequest
import com.vvieira.appauthenticator.domain.model.RegisterResponseOk
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
    override suspend fun loginPassword(user: LoginModelRequest): LoginResponseOk {
        return suspendCoroutine { continuation ->
            var responseLoginPassword = ""
            clientRetrofitWithRoutes.login("password", user).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body()?.string() ?: ""
                            val loginResponse: LoginResponseOk =
                                Gson().fromJson(jsonResponse, LoginResponseOk::class.java)
                            continuation.resumeWith(Result.success(loginResponse))
                        } else {
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

    override suspend fun registerUser(
        register: RegisterModelRequest,
        type: String
    ): RegisterResponseOk {
        return suspendCoroutine { continuation ->
            var responseregisterUser = ""
            clientRetrofitWithRoutes.register(type, register).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.i("RESPOSTA-OK", "${response.code()}")
                            val jsonResponse = response.body()?.string() ?: ""
                            Log.i("RESPOSTA-OK", "jsonResponse: $jsonResponse")
                            val registerResponse: RegisterResponseOk =
                                Gson().fromJson(jsonResponse, RegisterResponseOk::class.java)
                            continuation.resumeWith(Result.success(registerResponse))
                        } else {
                            val errMsg = response.message()
                            Log.i("RESPOSTA-ELSE", " CODE:  ${response.code()}")
                            Log.d("RESPOSTA-ELSE", "jsonResponse: $errMsg")
                            continuation.resumeWithException((Exception(errMsg)))
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
}
