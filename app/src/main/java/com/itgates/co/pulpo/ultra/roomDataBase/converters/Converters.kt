package com.itgates.co.pulpo.ultra.roomDataBase.converters

import androidx.room.TypeConverter
import com.itgates.co.pulpo.ultra.ui.utils.GiveawayModule
import com.itgates.co.pulpo.ultra.ui.utils.ManagerModule
import com.itgates.co.pulpo.ultra.ui.utils.ProductModule
import com.google.gson.Gson
import java.io.File

class Converters {
    @TypeConverter
    fun fromMultipleListsToString(roomMultipleListsModule: RoomMultipleListsModule): String {
        return Gson().toJson(roomMultipleListsModule)
    }

    @TypeConverter
    fun fromStringToMultipleLists(text: String): RoomMultipleListsModule {
        return Gson().fromJson(text, RoomMultipleListsModule::class.java)
    }
    @TypeConverter
    fun fromUriListToString(roomUriListModule: RoomPathListModule): String {
        return Gson().toJson(roomUriListModule)
    }

    @TypeConverter
    fun fromStringToUriList(text: String): RoomPathListModule {
        return Gson().fromJson(text, RoomPathListModule::class.java)
    }

    @TypeConverter
    fun fromFileToString(file: File): String {
        return Gson().toJson(file)
    }

    @TypeConverter
    fun fromStringTofile(text: String): File {
        return Gson().fromJson(text, File::class.java)
    }
}

data class RoomMultipleListsModule(
    val products: List<RoomProductModule>,
    val giveaways: List<RoomGiveawayModule>,
    val managers: List<RoomManagerModule>,
)

data class RoomProductModule(
    val productId: Long,
    val productName: String,
    val feedbackId: Long,
    val samples: Int,
    val comment: String,
    val followUp: String,
    val markedFeedback: String,
    val quotation: String,
    val quotationPaymentMethodId: Int,
    val isDemo: Boolean,
    val demoDate: String,
) {
    constructor(productModule: ProductModule): this(
        productModule.productCurrentValue.id, productModule.productCurrentValue.embedded.name,
        productModule.commentCurrentValue.id, productModule.sampleCurrentValue.id.toInt(),
        productModule.comment, productModule.followUp, productModule.markedFeedback,
        productModule.quotation, productModule.quotationPaymentMethodCurrentValue.id.toInt(),
        productModule.isDemo, productModule.demoDate
    )
}

data class RoomGiveawayModule(
    val giveawayId: Long,
    val samples: Int,
) {
    constructor(giveawayModule: GiveawayModule): this(
        giveawayModule.giveawayCurrentValue.id, giveawayModule.sampleCurrentValue.id.toInt()
    )
}

data class RoomManagerModule(
    val managerId: Long,
    val managerName: String
) {
    constructor(managerModule: ManagerModule): this(
        managerModule.managerCurrentValue.id, managerModule.managerCurrentValue.embedded.name
    )
}

data class RoomPathListModule(
    var paths: List<String>,
)