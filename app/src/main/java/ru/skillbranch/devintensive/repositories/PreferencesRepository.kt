package ru.skillbranch.devintensive.repositories

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.models.Profile

object PreferencesRepository {

    const val APP_THEME = "APP_THEME"

    private val prefs: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme() = prefs.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)

    fun saveProfile(profile: Profile) {
        with(profile) {
            putValue(Profile.FIELD_FIRST_NAME to firstName)
            putValue(Profile.FIELD_LAST_NAME to lastName)
            putValue(Profile.FIELD_ABOUT to about)
            putValue(Profile.FIELD_REPOSITORY to repository)
            putValue(Profile.FIELD_RATING to rating)
            putValue(Profile.FIELD_RESPECT to respect)
        }
    }

    fun getProfile(): Profile = Profile(
        prefs.getString(Profile.FIELD_FIRST_NAME, "")!!,
        prefs.getString(Profile.FIELD_LAST_NAME, "")!!,
        prefs.getString(Profile.FIELD_ABOUT, "")!!,
        prefs.getString(Profile.FIELD_REPOSITORY, "")!!,
        prefs.getInt(Profile.FIELD_RATING, 0),
        prefs.getInt(Profile.FIELD_RESPECT, 0)
    )

    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first
        val value = pair.second

        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in repository")
        }

        apply()
    }

}