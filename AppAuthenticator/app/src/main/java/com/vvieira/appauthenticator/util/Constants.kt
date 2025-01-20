package com.vvieira.appauthenticator.util

const val URL_API = "http://10.0.2.2"
const val API_PORT = ":8000"
const val URL_FULL = "$URL_API$API_PORT"

const val LOGIN = "$URL_FULL/user/login/{type}"
const val CADASTRO = "$URL_FULL/user/register/{type}"

const val DEFAUT_LOGIN = "password"
const val GOOGLE_LOGIN = "google"
const val FACEBOOK_LOGIN = "facebook"
const val BIOMETRIC_LOGIN = "biometric"