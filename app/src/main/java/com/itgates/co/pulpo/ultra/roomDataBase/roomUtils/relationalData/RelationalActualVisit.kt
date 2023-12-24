package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.ActualVisit

data class RelationalActualVisit(
    @Embedded val actualVisit: ActualVisit,
    val divName: String,
    val brickName: String?,
    val accTypeName: String,
    val accName: String,
    val docName: String,
    val accLLFirst: String,
    val accLGFirst: String,
)