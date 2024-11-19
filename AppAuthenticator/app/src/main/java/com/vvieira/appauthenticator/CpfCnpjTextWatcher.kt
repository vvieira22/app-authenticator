package com.vvieira.appauthenticator

import android.text.Editable
import android.text.TextWatcher
import kotlin.text.filter
import kotlin.text.isDigit
import kotlin.text.substring

class CpfCnpjTextWatcher : TextWatcher {
    private var isRunning = false
    private var isDeleting = false

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (isRunning || isDeleting) {
            return
        }
        isRunning = true

        val editable = s.toString()
        val digits = editable.filter { it.isDigit() }

        val formatted = when (digits.length) {
            0 -> ""
            in 1..3 -> digits
            in 4..6 -> "${digits.substring(0, 3)}.${digits.substring(3)}"
            in 7..9 -> "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6)}"
            in 10..11 -> "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${
                digits.substring(
                    6,
                    9
                )
            }-${digits.substring(9)}"

            in 12..14 -> "${digits.substring(0, 2)}.${digits.substring(2, 5)}.${
                digits.substring(
                    5,
                    8
                )
            }/${digits.substring(8)}"

            else -> {
                if (digits.length <= 11) { // CPF
                    "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${
                        digits.substring(
                            6,
                            9
                        )
                    }-${digits.substring(9, 11)}"
                } else { // CNPJ
                    "${digits.substring(0, 2)}.${digits.substring(2, 5)}.${
                        digits.substring(
                            5,
                            8
                        )
                    }/${digits.substring(8, 12)}-${digits.substring(12, 14)}"
                }
            }
        }

        s.replace(0, s.length, formatted, 0, formatted.length)
        isRunning = false
    }
}