package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomProductModule

data class ProductInfoModel(
    val product_id: Long,
    val samples: Int,
    val average: Int,
    val notes: String,
    val mfeedback: String,
    val followup: String,
    val followup_result: String,
    val followup_comments: String,
    val followup_date: String,
    val last_order_quantity: Int,
    val current_stock: Int,
    val current_order: Int,
    val feedback_id: Long,
) {
    constructor(roomProductModule: RoomProductModule): this(
        roomProductModule.productId,roomProductModule.samples, 0,
        roomProductModule.comment, roomProductModule.markedFeedback,
        roomProductModule.followUp, "", "", "",
        0, 0, 0, roomProductModule.feedbackId
    )
}