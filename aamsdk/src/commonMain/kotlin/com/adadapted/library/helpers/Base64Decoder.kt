package com.adadapted.library.helpers

import com.adadapted.library.constants.Config.LOG_TAG

object Base64 {
    private const val BASE64_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    private val BASE64_REGEX = "[^="+BASE64_SET+"]".toRegex()

    val String.base64encoded: String get() {
        val pad = when (this.length % 3) {
            1 -> "=="
            2 -> "="
            else -> ""
        }
        var raw = this
        (1 .. pad.length).forEach { _ -> raw += 0.toChar() }
        return StringBuilder().apply {
            (raw.indices step 3).forEach { it ->
                val n: Int = (
                        0xFF.and(raw[ it ].code) shl 16) +
                        (0xFF.and(raw[it+1].code) shl  8) +
                        0xFF.and(raw[it+2].code
                        )

                listOf(
                    (n shr 18) and 0x3F,
                    (n shr 12) and 0x3F,
                    (n shr  6) and 0x3F,
                    n and 0x3F
                ).forEach { append(BASE64_SET[it]) }
            }
        }   .dropLast(pad.length)
            .toString() + pad
    }

    val String.base64decoded: String get() {
        if (this.length % 4 != 0) throw IllegalArgumentException(LOG_TAG + "Cannot decode: $this is not compliant with BASE64 length requirement.")
        val clean = this.replace(BASE64_REGEX, "").replace("=", "A")
        val padLength = this.filter {it == '='}.length
        return StringBuilder().apply {
            (clean.indices step 4).forEach {
                val n: Int = (
                        BASE64_SET.indexOf(clean[it]) shl 18) +
                        (BASE64_SET.indexOf(clean[it+1]) shl 12) +
                        (BASE64_SET.indexOf(clean[it+2]) shl  6) +
                        BASE64_SET.indexOf(clean[it+3]
                        )

                listOf<Int>(
                    0xFF.and(n shr 16),
                    0xFF.and(n shr  8),
                    0xFF.and(   n    )
                ).forEach { append(it.toChar()) }
            }
        }   .dropLast(padLength)
            .toString()
    }
}