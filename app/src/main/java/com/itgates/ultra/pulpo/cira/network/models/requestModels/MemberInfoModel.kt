package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomManagerModule

data class MemberInfoModel(
    val emp_id: Long
) {
    constructor(roomManagerModule: RoomManagerModule): this(roomManagerModule.managerId)
}