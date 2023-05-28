package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit

data class RelationalActualVisit(
    @Embedded val actualVisit: ActualVisit,
    val divName: String,
    val brickName: String?,
    val accTypeName: String,
    val accName: String,
    val docName: String
)