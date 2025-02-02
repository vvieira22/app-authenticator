package com.vvieira.appauthenticator.util

const val URL_API = "http://10.0.2.2"
const val API_PORT = ":8000"
const val URL_FULL = "$URL_API$API_PORT"

const val LOGIN = "$URL_FULL/user/login/{type}"
const val CADASTRO = "$URL_FULL/user/register/{type}"

const val DEFAUT_AUTH = "password"
const val GOOGLE_AUTH = "google"
const val FACEBOOK_AUTH = "facebook"
const val BIOMETRIC_AUTH = "biometric"


//VAL DEFAULT REGISTER FIELDS
const val EMAIL = "email"
const val PASSWORD = "password"
const val NAME = "name"
const val FACEBOOK_ID = "facebookId"
const val GMAIL_ID = "gmailId"
const val BIOMETRIC_DATA = "biometricData"
const val DOCUMENT = "document"
const val BIRTHDAY = "birthday"
const val PHONE = "phone"

object HttpStatusCodes {
    // Sucesso
    const val HTTP_OK = 200
    const val HTTP_CREATED = 201
    const val HTTP_ACCEPTED = 202
    const val HTTP_NO_CONTENT = 204

    // Redirecionamento
    const val HTTP_MOVED_PERMANENTLY = 301
    const val HTTP_FOUND = 302
    const val HTTP_NOT_MODIFIED = 304

    // Erros do Cliente
    const val HTTP_BAD_REQUEST = 400
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_PAYMENT_REQUIRED = 402
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_METHOD_NOT_ALLOWED = 405
    const val HTTP_NOT_ACCEPTABLE = 406
    const val HTTP_REQUEST_TIMEOUT = 408
    const val HTTP_CONFLICT = 409
    const val HTTP_GONE = 410
    const val HTTP_LENGTH_REQUIRED = 411
    const val HTTP_PRECONDITION_FAILED = 412
    const val HTTP_PAYLOAD_TOO_LARGE = 413
    const val HTTP_URI_TOO_LONG = 414
    const val HTTP_UNSUPPORTED_MEDIA_TYPE = 415
    const val HTTP_RANGE_NOT_SATISFIABLE = 416
    const val HTTP_EXPECTATION_FAILED = 417
    const val HTTP_IM_A_TEAPOT = 418 // Easter egg!

    // Erros do Servidor
    const val HTTP_INTERNAL_SERVER_ERROR = 500
    const val HTTP_NOT_IMPLEMENTED = 501
    const val HTTP_BAD_GATEWAY = 502
    const val HTTP_SERVICE_UNAVAILABLE = 503
    const val HTTP_GATEWAY_TIMEOUT = 504
    const val HTTP_HTTP_VERSION_NOT_SUPPORTED = 505
}