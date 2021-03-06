package ru.skillbranch.devintensive.utils

import java.util.*

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = if (fullName.isNullOrBlank()) null else fullName.trim().split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    /**
     * @param firstName имя
     * @param lastName фамилия
     * @return строка с первыми буквами имени и фамилии в верхнем регистре.
     * Если один из аргументов null, то возвращает один инициал, если оба аргумента null возвращает null)
     */
    fun toInitials(firstName: String?, lastName: String?): String? {
        val first = firstName?.trim()?.capitalize()?.getOrNull(0)
        val last = lastName?.trim()?.capitalize()?.getOrNull(0)
        return if (first == null && last == null) null else "${first ?: ""}${last ?: ""}"
    }

    /**
     * @param payload - исходная строка
     * @param divider - разделитель, по умолчанию пробел
     * @return Преобразованная строка из латинских символов
     */
    fun transliteration(payload: String?, divider: String = " "): String? = payload?.trim()?.replaceChar { src ->
        when (src) {
            " " -> divider
            "а" -> "a"
            "б" -> "b"
            "в" -> "v"
            "г" -> "g"
            "д" -> "d"
            "е", "ё", "э" -> "e"
            "ж" -> "zh"
            "з" -> "z"
            "и", "й", "ы" -> "i"
            "к" -> "k"
            "л" -> "l"
            "м" -> "m"
            "н" -> "n"
            "о" -> "o"
            "п" -> "p"
            "р" -> "r"
            "с" -> "s"
            "т" -> "t"
            "у" -> "u"
            "ф" -> "f"
            "х" -> "h"
            "ц" -> "c"
            "ч" -> "ch"
            "ш" -> "sh"
            "щ" -> "sh'"
            "ъ", "ь", "Ъ", "Ь" -> ""
            "ю" -> "yu"
            "я" -> "ya"
            "А" -> "A"
            "Б" -> "B"
            "В" -> "V"
            "Г" -> "G"
            "Д" -> "D"
            "Е", "Ё", "Э" -> "E"
            "Ж" -> "Zh"
            "З" -> "Z"
            "И", "Й", "Ы" -> "I"
            "К" -> "K"
            "Л" -> "L"
            "М" -> "M"
            "Н" -> "N"
            "О" -> "O"
            "П" -> "P"
            "Р" -> "R"
            "С" -> "S"
            "Т" -> "T"
            "У" -> "U"
            "Ф" -> "F"
            "Х" -> "H"
            "Ц" -> "C"
            "Ч" -> "Ch"
            "Ш" -> "Sh"
            "Щ" -> "Sh'"
            "Ю" -> "Yu"
            "Я" -> "Ya"
            else -> src
        }
    }

    /**
     * Преобразование символов в строке
     * @param predicate функция преобразования
     * @return Новая строка
     */
    private fun String.replaceChar(predicate: (String) -> String): String {
        val sb = StringBuilder()
        for (index in 0 until length) {
            val char = get(index)
            sb.append(predicate(char.toString()))
        }
        return sb.toString()
    }

    private val githubExceptions = setOf("enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    )

    private val githubPrefixes = setOf(
        "https://github.com/",
        "https://www.github.com/",
        "www.github.com/",
        "github.com/"
    )

    fun isGithubUrlValid(url: String): Boolean {
        if (url.isEmpty()) {
            return true
        }
        val trimmedUrl = url.trim()
        val prefix = githubPrefixes.filter { trimmedUrl.startsWith(it, true) }
        if (prefix.isEmpty()) {
            return false
        } else {
            var userName = trimmedUrl.replaceFirst(prefix[0], "", true)
            if (userName.endsWith("/")) {
                if (userName == "/") {
                    return false
                }
                userName = userName.slice(0..userName.length - 2)
            }
            // Реальное валидное имя github-аккаунта
            if (!userName.toLowerCase(Locale.ROOT).matches("^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){0,38}\$".toRegex())) {
                return false
            }
            return !githubExceptions.contains(userName)
        }
    }
}