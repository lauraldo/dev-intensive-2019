package ru.skillbranch.devintensive.extensions

fun String.truncate(max: Int = 16) = if (max >= trim().length) trim() else "${substring(0 until max).trim()}..."

fun String.stripHtml() = replace("<[^>]*>".toRegex(), "").replace("\\s+".toRegex(), " ")