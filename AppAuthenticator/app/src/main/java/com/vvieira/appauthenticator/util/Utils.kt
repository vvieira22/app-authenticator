package com.vvieira.appauthenticator.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.isDigitsOnly
import com.google.android.material.snackbar.Snackbar
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_DEFAULT_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_FACEBOOK_AND_DEFAULT_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_FACEBOOK_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_GOOGLE_AND_DEFAULT_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.ALREADY_GOOGLE_REGISTERED
import com.vvieira.appauthenticator.util.SOCIAL_AUTH_ERROS.NOT_REGISTERED_YET
import java.util.Locale

class Utils {
    companion object {
        fun String.isValidCPF(): Boolean {
            val cpfClean = this.replace(".", "").replace("-", "")

            if (cpfClean.length != 11 || cpfClean.all { it == cpfClean[0] }) {
                return false
            }

            val digits = cpfClean.map { it.toString().toInt() }
            val dv1 = calculateDV(digits.subList(0, 9), 10)
            val dv2 = calculateDV(digits.subList(0, 10), 11)

            return dv1 == digits[9] && dv2 == digits[10]
        }

        fun String.isValidCNPJ(): Boolean {
            val cnpjClean = this.replace(".", "").replace("/", "").replace("-", "")

            if (cnpjClean.length != 14 || cnpjClean.all { it == cnpjClean[0] }) {
                return false
            }

            val digits = cnpjClean.map { it.toString().toInt() }
            val dv1 = calculateDV(digits.subList(0, 12), 5)
            val dv2 = calculateDV(digits.subList(0, 13), 6)

            return dv1 == digits[12] && dv2 == digits[13]
        }

        fun String.isCpf(): Boolean {
            return (this.length <= 11)
        }

        private fun calculateDV(digits: List<Int>, initialMultiplier: Int): Int {
            var sum = 0
            var multiplier = initialMultiplier

            for (digit in digits) {
                sum += digit * multiplier
                multiplier--
                if (multiplier < 2) {
                    multiplier = 9
                }
            }

            val remainder = sum % 11
            return if (remainder < 2) 0 else 11 - remainder
        }

        fun customSnackBar(
            view: View,
            mensagem: String,
            cor: Int, corTexto: Int,
            time: Int = Snackbar.LENGTH_LONG,
            show: Boolean = true
        ): Snackbar {
            val snackbar = Snackbar.make(view, mensagem, time)
            snackbar.setBackgroundTint(cor)
            snackbar.setTextColor(corTexto)
            if (show) snackbar.show()
            return snackbar
        }

        fun valEmail(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            return emailRegex.matches(email)
        }

        fun valPassword(password: String): Boolean {
            return password.length >= 6
        }

        fun showLoadingDialog(context: Context): Dialog {
            val progressDialog = Dialog(context)

            progressDialog.let {
                it.show()
                it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                it.setContentView(R.layout.progress_dialog)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                return it
            }
        }

        fun convertErroToTextMessage(
            response: String,
            context: Context,
            needMoreInformation: String = "" //TODO Maybe add what type information need (isAuthLoginInvolved like), to transcribe correctly the inf.
        ): String {
            if (response.isDigitsOnly()) {
                val codigo = response.toInt()
                when (codigo) {
                    400 -> {
                        return context.getString(R.string.http_400)
                    }

                    401 -> {
                        return context.getString(R.string.http_401)
                    }

                    403 -> {
                        return context.getString(R.string.http_403)
                    }

                    404 -> {
                        if (needMoreInformation.isNotEmpty()) { //TODO For now, continue like string to maybe in future pass type of needMoreInformation
                            return isAuthLoginInvolved(response).toString()
                        } else {
                            return context.getString(R.string.http_404)
                        }
                    }

                    500 -> {
                        return context.getString(R.string.http_500)
                    }

                    502 -> {
                        return context.getString(R.string.http_502)
                    }

                    503 -> {
                        return context.getString(R.string.http_503)
                    }

                    504 -> {
                        return context.getString(R.string.http_504)
                    }

                    200 -> {
                        if (needMoreInformation.isNotEmpty()) { //TODO For now, continue like string to maybe in future pass type of needMoreInformation
                            return isAuthLoginInvolved(response).toString() //200 for socialAuth() and need know of user type is social authenticated.
                        } else {
                            return context.getString(R.string.http_success_login_200)
                        }
                    }

                    201 -> {
                        return context.getString(R.string.http_success_signup_201)
                    }

                    204 -> {
                        return context.getString(R.string.http_success_logout_204)
                    }

                    202 -> {
                        return context.getString(R.string.http_success_request_sent_202)
                    }

                    else -> {
                        return context.getString(R.string.unknown_error)
                    }
                }
            } else {
                if (response != "") {
                    return when {
                        isErrorConnection(response) -> context.getString(R.string.host_out_of_reach)
                        else -> {
                            context.getString(R.string.unknown_error)
                        }
                    }
                }
            }
            return context.getString(R.string.unknown_error)
        }

        private fun isErrorConnection(response: String): Boolean {
            //TODO Maybe check if is there a function to find (connection && failed) together with less words
            val regex =
                Regex(".*\\b(connect|connection|connecting|conectar|conexão)\\b.*\\b(failed|failure|failing|falhou|falha)\\b.*|.*\\b(failed|failure|failing|falhou|falha)\\b.*\\b(connect|connection|connecting|conectar|conexão)\\b.*")
            return regex.matches(response.lowercase(Locale.getDefault()))
        }

        private fun isAuthLoginInvolved(response: String): String? {
            return when {
                (response.matches(Regex(NOT_REGISTERED_YET))) -> NOT_REGISTERED_YET
                (response.matches(Regex(ALREADY_GOOGLE_REGISTERED))) -> ALREADY_GOOGLE_REGISTERED
                (response.matches(Regex(ALREADY_GOOGLE_AND_DEFAULT_REGISTERED))) -> ALREADY_GOOGLE_AND_DEFAULT_REGISTERED
                (response.matches(Regex(ALREADY_FACEBOOK_REGISTERED))) -> ALREADY_FACEBOOK_REGISTERED
                (response.matches(Regex(ALREADY_FACEBOOK_AND_DEFAULT_REGISTERED))) -> ALREADY_FACEBOOK_AND_DEFAULT_REGISTERED
                (response.matches(Regex(ALREADY_DEFAULT_REGISTERED))) -> ALREADY_DEFAULT_REGISTERED
                else -> null
            }
        }
    }
}