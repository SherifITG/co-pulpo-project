package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Line

data class RelationalLine(
    @Embedded val line: Line,
    val unplannedCoverage: Int
) {
    fun isLineUnplannedLimitComplete(): Boolean {
        return unplannedCoverage >= line.unplannedLimit
    }
}