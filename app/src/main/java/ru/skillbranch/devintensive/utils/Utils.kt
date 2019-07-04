package ru.skillbranch.devintensive.utils

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
    fun transliteration(payload: String?, divider: String = " "): String? = payload?.replaceChar { src ->
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
    inline fun String.replaceChar(predicate: (String) -> String): String {
        val sb = StringBuilder()
        for (index in 0 until length) {
            val char = get(index)
            sb.append(predicate(char.toString()))
        }
        return sb.toString()
    }

    /**
     * Выбирает для слова нужную форму множественного числа в зависимости от количества
     * @param amount число
     * @param text1 вариант слова, если количество оканчивается 1
     * @param text2_4 вариант слова, если количество оканчивается 2, 3 или 4
     * @param other иной вариант слова
     * @return Строковое представление слова во множественном числе
     */
    fun declension(amount: Long, text1: String, text2_4: String, other: String): String {
        val mod10 = amount % 10
        val mod100 = amount % 100

        return when {
            mod100 in 11 until 20 -> other
            mod10 == 1L -> text1
            mod10 in 2..4 -> text2_4
            else -> other
        }
    }
}