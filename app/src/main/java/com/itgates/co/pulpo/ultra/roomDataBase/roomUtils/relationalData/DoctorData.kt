package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor

class DoctorData (
    @Embedded val doctor: Doctor,
    val specialityName: String?
)