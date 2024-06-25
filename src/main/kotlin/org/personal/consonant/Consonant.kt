package org.personal.consonant

import java.text.Normalizer
import java.util.regex.Pattern
import kotlin.streams.asSequence

class Consonant(
    val origin: String
) {

    private val normalizes: List<String> = this.origin.normalize()

    val first = normalizes.first()
    val middle = normalizes[1]
    val last = normalizes.getOrNull(2)

    private fun String.normalize(): List<String> {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)

        return normalized.codePoints().asSequence()
            .map { String.format("\\u%04X", it).decodeFromUnicode() }.toList()
    }

    private fun String.decodeFromUnicode(): String {
        val pattern = Pattern.compile("\\\\u[0-9a-fA-F]{4}")
        val matcher = pattern.matcher(this)

        val decoded = StringBuilder()

        while (matcher.find()) {
            val unicodeSequence = matcher.group()
            val unicodeChar = unicodeSequence.substring(2).toInt(16).toChar()

            matcher.appendReplacement(decoded, unicodeChar.toString())
        }

        matcher.appendTail(decoded)

        return decoded.toString()
    }
}
