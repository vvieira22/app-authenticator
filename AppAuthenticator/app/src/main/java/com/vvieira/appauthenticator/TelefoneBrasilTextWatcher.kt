package com.vvieira.appauthenticator

import android.text.Editable
import android.text.TextWatcher

class TelefoneBrasilTextWatcher : TextWatcher {

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
            in 1..2 -> "($digits)"
            in 3..6 -> "(${digits.substring(0, 2)}) ${digits.substring(2)}"
            in 7..11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
            else -> {
                // Número inválido, remover último dígito
                s.delete(s.length - 1, s.length)
                isRunning = false
                return
            }
        }

        s.replace(0, s.length, formatted, 0, formatted.length)
        isRunning = false
    }
}