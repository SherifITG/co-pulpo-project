package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor

class DoctorData (
    @Embedded val doctor: Doctor,
    val specialityName: String?
)