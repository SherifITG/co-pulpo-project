package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.ActualVisit
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.OfficeWork

data class RelationalOfficeWorkReport(
    @Embedded val officeWork: OfficeWork,
    val officeWorkName: String?
)