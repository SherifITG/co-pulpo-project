package com.itgates.ultra.pulpo.cira.roomDataBase.converters

import androidx.room.TypeConverter
import com.itgates.ultra.pulpo.cira.ui.utils.GiveawayModule
import com.itgates.ultra.pulpo.cira.ui.utils.ManagerModule
import com.itgates.ultra.pulpo.cira.ui.utils.ProductModule
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
) {
    constructor(productModule: ProductModule): this(
        productModule.productCurrentValue.id, productModule.productCurrentValue.embedded.name,
        productModule.commentCurrentValue.id, productModule.sampleCurrentValue.id.toInt(),
        productModule.comment, productModule.followUp, productModule.markedFeedback
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