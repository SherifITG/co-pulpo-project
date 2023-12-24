package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomGiveawayModule

data class GiveawayInfoModel(
    val giveaway_id: Long,
    val units: Int
) {
    constructor(roomGiveawayModule: RoomGiveawayModule): this(
        roomGiveawayModule.giveawayId,roomGiveawayModule.samples
    )
}