package com.itgates.co.pulpo.ultra.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val IS_MANAGER = stringPreferencesKey("IS_MANAGER")
    val TYPE_ID = stringPreferencesKey("TYPE_ID")
    val USER_ID =stringPreferencesKey("USER_ID")
    val CODE =stringPreferencesKey("CODE")
    val FULL_NAME =stringPreferencesKey("FULL_NAME")
    val USERNAME =stringPreferencesKey("USERNAME")
    val URL =stringPreferencesKey("URL")
    val TOKEN =stringPreferencesKey("TOKEN")
    val LAST_LOGIN =stringPreferencesKey("LAST_LOGIN")
    val CHECK_IN_TRIP_START = booleanPreferencesKey("CHECK_IN_TRIP_START")

    val REMEMBER_ME =stringPreferencesKey("REMEMBER_ME")
    val CACHE_LOCATION =stringPreferencesKey("CACHE_LOCATION")

    val CONFIGS =stringPreferencesKey("CONFIGS")

}