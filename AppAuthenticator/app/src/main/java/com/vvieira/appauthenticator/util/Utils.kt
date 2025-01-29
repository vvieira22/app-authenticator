package com.vvieira.appauthenticator.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.snackbar.Snackbar
import com.vvieira.appauthenticator.R

class Utils {
    companion object {
        fun isValidCPF(cpf: String): Boolean {
            val cpfClean = cpf.replace(".", "").replace("-", "")

            if (cpfClean.length != 11 || cpfClean.all { it == cpfClean[0] }) {
                return false
            }

            val digits = cpfClean.map { it.toString().toInt() }
            val dv1 = calculateDV(digits.subList(0, 9), 10)
            val dv2 = calculateDV(digits.subList(0, 10), 11)

            return dv1 == digits[9] && dv2 == digits[10]
        }

        fun isValidCNPJ(cnpj: String): Boolean {
            val cnpjClean = cnpj.replace(".", "").replace("/", "").replace("-", "")

            if (cnpjClean.length != 14 || cnpjClean.all { it == cnpjClean[0] }) {
                return false
            }

            val digits = cnpjClean.map { it.toString().toInt() }
            val dv1 = calculateDV(digits.subList(0, 12), 5)
            val dv2 = calculateDV(digits.subList(0, 13), 6)

            return dv1 == digits[12] && dv2 == digits[13]
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

         fun customSnackBar(view: View,
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

    }
}