package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomManagerModule

data class MemberInfoModel(
    val emp_id: Long
) {
    constructor(roomManagerModule: RoomManagerModule): this(roomManagerModule.managerId)
}