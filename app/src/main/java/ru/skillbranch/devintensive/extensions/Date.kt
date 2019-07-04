package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val SECOND = 1000L
const val MINUTE = SECOND * 60
const val HOUR = MINUTE * 60
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

/**
 * Форматирование вывода разницы между датами в человекопонятном формате, с учетом склонения числительных
 * @receiver Дата, которая сравнивается с параметром или с актуальной датой
 * @param date дата, с которой сравнивается текущий объект, по умолчанию актуальная дата
 * @return Строка с разницей между датами, понятной человеку
 *
 */
fun Date.humanizeDiff(date: Date = Date()): String {

    val delta = date.time - this.time
    return when {
        delta > 0 -> when (delta) {
            in 0 until SECOND -> "только что"
            in SECOND until 45 * SECOND -> "несколько секунд назад"
            in 45 * SECOND until 75 * SECOND -> "минуту назад"
            in 75 * SECOND until 45 * MINUTE -> {
                val mins = (delta / MINUTE.toDouble()).roundToInt()
                "${TimeUnits.MINUTE.plural(mins)}} назад"
            }
            in 45 * MINUTE until 75 * MINUTE -> "час назад"
            in 75 * MINUTE until 22 * HOUR -> {
                val hrs = (delta / HOUR.toDouble()).roundToInt()
                "${TimeUnits.HOUR.plural(hrs)} назад"
            }
            in 22 * HOUR until 26 * HOUR -> "день назад"
            in 26 * HOUR until 360 * DAY -> {
                val days = (delta / DAY.toDouble()).roundToInt()
                "${TimeUnits.DAY.plural(days)} назад"
            }
            in 360 * DAY..Long.MAX_VALUE -> "более года назад"
            else -> "неизвестно когда"
        }
        delta < 0 -> when (-delta) {
            in 0 until SECOND -> "только что"
            in SECOND until 45 * SECOND -> "через несколько секунд"
            in 45 * SECOND until 75 * SECOND -> "через минуту"
            in 75 * SECOND until 45 * MINUTE -> {
                val mins = (-delta / MINUTE.toDouble()).roundToInt()
                "через ${TimeUnits.MINUTE.plural(mins)}"
            }
            in 45 * MINUTE until 75 * MINUTE -> "через час"
            in 75 * MINUTE until 22 * HOUR -> {
                val hrs = (-delta / HOUR.toDouble()).roundToInt()
                "через ${TimeUnits.HOUR.plural(hrs)}"
            }
            in 22 * HOUR until 26 * HOUR -> "через день"
            in 26 * HOUR until 360 * DAY -> {
                val days = (-delta / DAY.toDouble()).roundToInt()
                "через ${TimeUnits.DAY.plural(days)}"
            }
            in 360 * DAY..Long.MAX_VALUE -> "более чем через год"
            else -> "неизвестно когда"
        }
        else -> "только что"
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int): String {

        val mod10 = value % 10
        val mod100 = value % 100

        return when (this) {
            SECOND -> when {
                mod100 in 11 until 20 -> "$value секунд"
                mod10 == 1 -> "$value секунду"
                mod10 in 2..4 -> "$value секунды"
                else -> "$value секунд"
            }
            MINUTE -> when {
                mod100 in 11 until 20 -> "$value минут"
                mod10 == 1 -> "$value минуту"
                mod10 in 2..4 -> "$value минуты"
                else -> "$value минут"
            }
            HOUR -> when {
                mod100 in 11 until 20 -> "$value часов"
                mod10 == 1 -> "$value час"
                mod10 in 2..4 -> "$value часа"
                else -> "$value часов"
            }
            DAY -> when {
                mod100 in 11 until 20 -> "$value дней"
                mod10 == 1 -> "$value день"
                mod10 in 2..4 -> "$value дня"
                else -> "$value дней"
            }
        }
    }
}