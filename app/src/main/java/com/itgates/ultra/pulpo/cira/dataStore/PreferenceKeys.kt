package com.itgates.ultra.pulpo.cira.dataStore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val IS_MANAGER = stringPreferencesKey("IS_MANAGER")
    val DIVISIONS_IDS = stringPreferencesKey("DIVISIONS_IDS")
    val DIVISIONS_NAME =stringPreferencesKey("DIVISION_NAME")
    val LINE_NAME =stringPreferencesKey("LINE_NAME")
    val LINES_IDS =stringPreferencesKey("LINES_IDS")
    val USER_ID =stringPreferencesKey("USER_ID")
    val CODE =stringPreferencesKey("CODE")
    val USERNAME =stringPreferencesKey("USERNAME")
    val NAME =stringPreferencesKey("NAME")
    val LAST_LOGIN =stringPreferencesKey("LAST_LOGIN")

    val REMEMBER_ME =stringPreferencesKey("REMEMBER_ME")
    val CACHE_LOCATION =stringPreferencesKey("CACHE_LOCATION")
}