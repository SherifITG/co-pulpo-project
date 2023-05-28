package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomGiveawayModule

data class GiveawayInfoModel(
    val gift_id: Long,
    val noofunits: Int
) {
    constructor(roomGiveawayModule: RoomGiveawayModule): this(
        roomGiveawayModule.giveawayId,roomGiveawayModule.samples
    )
}