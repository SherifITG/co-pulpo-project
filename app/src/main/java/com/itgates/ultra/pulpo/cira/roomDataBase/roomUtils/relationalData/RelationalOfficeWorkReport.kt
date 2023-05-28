package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit

data class RelationalOfficeWorkReport(
    @Embedded val actualVisit: ActualVisit,
    val officeWorkName: String?
)