package com.vvieira.appauthenticator.util

import AuthEndPoint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import okhttp3.logging.HttpLoggingInterceptor

class RetrofitUtils {

//    companion object {
//
//        /** Retorna uma Instância do Client Retrofit para Requisições
//         * @param path Caminho Principal da API
//         */
//        fun getRetrofitInstance(path : String) : AuthEndPoint {
////            val logging = HttpLoggingInterceptor()
////            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//            val client = OkHttpClient.Builder()
////                .addInterceptor(logging)
//                .build()
//
//            val retrofit =Retrofit.Builder()
//                .baseUrl(path)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            val endpoint = retrofit.create(AuthEndPoint::class.java)
//            return endpoint
//        }
//    }
}

