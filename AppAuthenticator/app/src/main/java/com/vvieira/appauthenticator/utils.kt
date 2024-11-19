package com.vvieira.appauthenticator

class utils {
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
    }
}