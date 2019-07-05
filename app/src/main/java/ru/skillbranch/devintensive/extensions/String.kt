package ru.skillbranch.devintensive.extensions

fun String.truncate(max: Int = 16) = if (max >= trimEnd().length) trimEnd() else "${substring(0 until max).trimEnd()}..."

fun String.stripHtml() = replace("<[^>]*>".toRegex(), "").replace("\\s+".toRegex(), " ")
