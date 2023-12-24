package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomProductModule

data class ProductInfoModel(
    val product_id: Long,
    val samples: Int,
    val notes: String,
    val market_feedback: String,
    val followup: String,
    val feedback_id: Long,
    val quotation: String,
    val quotation_payment_method: Int,
    val is_demo: Int,
    val demo_date: String,
) {
    constructor(roomProductModule: RoomProductModule): this(
        roomProductModule.productId,roomProductModule.samples, roomProductModule.comment,
        roomProductModule.markedFeedback, roomProductModule.followUp, roomProductModule.feedbackId,
        roomProductModule.quotation, roomProductModule.quotationPaymentMethodId,
        if (roomProductModule.isDemo) 1 else 0, roomProductModule.demoDate
    )
}