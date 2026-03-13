package com.cashproject.mongsil.kmp.screen.setting.screenlock

object PasswordHasher {
    fun hash(raw: String): String {
        val primary = fnv1a64(raw.encodeToByteArray())
        val secondary = fnv1a64(raw.reversed().encodeToByteArray())
        return primary.toString(16).padStart(16, '0') +
            secondary.toString(16).padStart(16, '0')
    }

    private fun fnv1a64(bytes: ByteArray): ULong {
        var hash = 0xcbf29ce484222325uL
        val prime = 0x100000001b3uL
        bytes.forEach { byte ->
            hash = (hash xor byte.toUByte().toULong()) * prime
        }
        return hash
    }
}
