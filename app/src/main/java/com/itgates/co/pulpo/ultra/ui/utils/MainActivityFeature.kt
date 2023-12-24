package com.itgates.co.pulpo.ultra.ui.utils

import androidx.annotation.DrawableRes

data class MainActivityFeature (
    @DrawableRes val icon: Int,
    val name: String,
    val action: () -> Unit,
) {
    fun isActualFeature(): Boolean {
        return this.name == "Unplanned\nVisit"
    }

    fun isPlannedFeature(): Boolean {
        return this.name == "Planned\nVisit"
    }
}