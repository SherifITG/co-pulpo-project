package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Account

class AccountData (
    @Embedded val account: Account,
    val divName: String,
    val accTypeName: String,
    val className: String?, // TODO nullable
    val brickName: String?
)