package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    val rank: String = "Junior Android Developer"
    val nickName: String
        get() = if (firstName.isBlank() && lastName.isBlank()) ""
                else Utils.transliteration("$firstName $lastName", "_") ?: ""

    fun toMap(): Map<String, Any> = mapOf(
        FIELD_NICKNAME to nickName,
        FIELD_RANK to rank,
        FIELD_FIRST_NAME to firstName,
        FIELD_LAST_NAME to lastName,
        FIELD_ABOUT to about,
        FIELD_REPOSITORY to repository,
        FIELD_RATING to rating,
        FIELD_RESPECT to respect
    )

    companion object {
        const val FIELD_NICKNAME = "nickName"
        const val FIELD_RANK = "rank"
        const val FIELD_FIRST_NAME = "first_name"
        const val FIELD_LAST_NAME = "last_name"
        const val FIELD_ABOUT = "about"
        const val FIELD_REPOSITORY = "repository"
        const val FIELD_RATING = "rating"
        const val FIELD_RESPECT = "respect"
    }

}