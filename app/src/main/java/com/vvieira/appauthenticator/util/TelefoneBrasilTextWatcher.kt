package com.vvieira.appauthenticator.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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


class MyMaskGeneric(var editText: EditText, protected var mMask: String) : TextWatcher {

    private var isUpdating: Boolean = false
    protected var mOldString = ""
    internal var befores = ""

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        befores = s.toString().replace("[^\\d]".toRegex(), "")

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        var str = s.toString().replace("[^\\d]".toRegex(), "")

        if (str.length == 0) {
            return
        }

        if (before == 1 && befores.length > 0 && !isUpdating) {
            val last = befores.substring(befores.length, befores.length)
            val rep = last.replace("(", "").replace(")", "").replace(" ", "").replace("-", "")
            if (rep.length == 0) {
                str = str.substring(0, befores.length - 1)
            }
        }


        val mask = StringBuilder()
        if (isUpdating) {
            mOldString = str
            isUpdating = false
            return
        }
        var i = 0
        for (m in mMask.toCharArray()) {
            if (m != '#') {
                mask.append(m)
                continue
            }
            try {
                mask.append(str[i])
            } catch (e: Exception) {
                break
            }

            i++
        }
        isUpdating = true
        val x = mask.toString()
        editText.setText(x)
        editText.setSelection(mask.length)

    }

    override fun afterTextChanged(s: Editable) {
    }
}