package com.itgates.co.pulpo.ultra.network.models.responseModels.responses

import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Account
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.PlannedVisit
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.google.gson.annotations.SerializedName
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.PlannedOW
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.MultiplicityEnum
import kotlin.streams.toList

data class UserDetailsData(
    @SerializedName("id")val id: Long,
    @SerializedName("emp_code")val empCode: String?,
    @SerializedName("username") val username: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("url")val imageUrl: String,
    @SerializedName("is_manager")val IsManager: Int,
)

data class OnlineMasterData(
    @SerializedName("account_types") val accTypes: ArrayList<OnlineAccountTypeData>,
    @SerializedName("lines") val lines: ArrayList<OnlineLineData>,
    @SerializedName("divisions") val divisions: ArrayList<OnlineDivisionData>,
    @SerializedName("bricks") val bricks: ArrayList<OnlineBrickData>,
    @SerializedName("products") val products: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("giveaways") val giveaways: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("managers") val managers: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("visitFeedBack") val feedbacks: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("office_work_types") val officeWorkTypes: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("classes") val classes: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("settings") val settings: ArrayList<OnlineSettingData>,
    @SerializedName("specialties") val specialties: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("vacation_types") val vacationTypes: ArrayList<OnlineVacationTypeData>,
) {
    constructor() : this(
        ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(),
        ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList()
    )

    fun collectAllIdAndNameRoomObjects(): List<IdAndNameEntity> {
        return products.stream().map { it.toRoomProduct() }.toList()
                .asSequence()
                .plus(giveaways.stream().map { it.toRoomGiveaway() }.toList())
                .plus(managers.stream().map { it.toRoomManager() }.toList())
                .plus(feedbacks.stream().map { it.toRoomFeedback() }.toList())
                .plus(officeWorkTypes.stream().map { it.toRoomOfficeWorkTypes() }.toList())
                .plus(classes.stream().map { it.toRoomClass() }.toList())
                .plus(specialties.stream().map { it.toRoomSpeciality() }.toList())
                .toList()
    }

    fun collectAccTypeRoomObjects(): List<AccountType> {
        return accTypes.stream().map { it.toRoomAccType() }.toList()
    }

    fun collectLineRoomObjects(): List<Line> {
        return lines.stream().map { it.toRoomLine() }.toList()
    }

    fun collectBrickRoomObjects(): List<Brick> {
        return bricks.stream().map { it.toRoomBrick() }.toList()
    }

    fun collectDivisionRoomObjects(): List<Division> {
        return divisions.stream().map { it.toRoomDivision() }.toList()
    }

    fun collectSettingRoomObjects(): List<Setting> {
        return settings.stream().map { it.toRoomSetting() }.toList()
    }

    fun collectVacationTypeRoomObjects(): List<VacationType> {
        return vacationTypes.stream().map { it.toRoomVacationType() }.toList()
    }
}

data class OnlineAccountsAndDoctorsData(
    @SerializedName("accounts") val accounts: ArrayList<OnlineAccountData>,
    @SerializedName("doctors") val doctors: ArrayList<OnlineDoctorData>
) {
    constructor() : this(ArrayList(), ArrayList())

    fun collectAccountRoomObjects(): List<Account> {
        return accounts.stream().map { it.toRoomAccount() }.toList()
    }

    fun collectDoctorRoomObjects(): List<Doctor> {
        return doctors.stream().map { it.toRoomDoctor() }.toList()
    }
}

data class OnlinePresentationsAndSlidesData(
    @SerializedName("presentations") val presentations: ArrayList<OnlinePresentationData>,
    @SerializedName("slides") val slides: ArrayList<OnlineSlideData>
) {
    constructor() : this(ArrayList(), ArrayList())

    fun collectPresentationRoomObjects(): List<Presentation> {
        return presentations.stream().map { it.toRoomPresentation() }.toList()
    }

    fun collectSlideRoomObjects(): List<Slide> {
        return slides.stream().map { it.toRoomSlide() }.toList()
    }
}

data class OnlineAccountTypeData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("sort") val sort: Int,
    @SerializedName("shift_id") val shiftId: Int,
    @SerializedName("accepted_distance") val accepted_distance: Int?,
) {
    fun toRoomAccType(): AccountType = AccountType(
        this.id,
        EmbeddedEntity(this.name.trim()),
        this.sort,
        this.shiftId,
        this.accepted_distance ?: -1
    )
}

data class OnlineLineData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("unplanned_limit") val unplannedLimit: Int,
) {
    fun toRoomLine(): Line = Line(
        this.id,
        EmbeddedEntity(this.name.trim()),
        this.unplannedLimit
    )
}

data class OnlineDivisionData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val LineId: Long,
    @SerializedName("type_id") val typeId: Long
) {
    fun toRoomDivision(): Division = Division(
        this.id,
        EmbeddedEntity(this.name.trim()),
        this.LineId,
        this.typeId
    )
}

data class OnlineBrickData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val lineId: String,
    @SerializedName("line_division_id") val ter_id: String
) {
    fun toRoomBrick(): Brick = Brick(this.id, EmbeddedEntity(this.name.trim()), this.lineId.trim(), this.ter_id.trim())
}

data class OnlineSettingData(
    @SerializedName("id") val id: Long,
    @SerializedName("attribute_name") val attribute_name: String,
    @SerializedName("attribute_value") val attribute_value: String,
) {
    fun toRoomSetting(): Setting = Setting(
        this.id, EmbeddedEntity(this.attribute_name.trim()), this.attribute_value.trim()
    )
}

data class OnlineVacationTypeData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("is_attach_required") val is_attach_required: Int?
) {
    fun toRoomVacationType(): VacationType = VacationType(
        this.id, EmbeddedEntity(this.name.trim()),  (is_attach_required ?: 0) == 1
    )
}

// include classes such as Product & Giveaway & Manager & Feedback & OfficeWorkTypes & Class & Speciality
data class OnlineIdAndNameObjectData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val lineId: Long?,
) {
    fun toRoomProduct() = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.PRODUCT, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomGiveaway() = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.GIVEAWAY, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomManager(): IdAndNameEntity = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.MANAGER, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomFeedback() = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.FEEDBACK, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomOfficeWorkTypes() = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomClass(): IdAndNameEntity = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.CLASS, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
    fun toRoomSpeciality() = IdAndNameEntity(
        this.id, IdAndNameTablesNamesEnum.SPECIALITY, EmbeddedEntity(this.name.trim()), lineId ?: -2
    )
}

data class OnlineAccountData( // 5 IDs PK
    val id: Long,
    val name: String,
    val line_id: Long,
    val div_id: Long,
    val class_id: Long,
    val brick_id: Long,
//    val code: String,
    val type_id: Int,
    val address: String,
    val tel: String,
    val mobile: String,
    val email: String,
    val ll: String,
    val lg: String
) {
    fun toRoomAccount(): Account {
        return Account(
            id, EmbeddedEntity(name.trim()), line_id, div_id, class_id,
            brick_id, type_id, address.trim(), tel.trim(), mobile.trim(), email.trim(), ll.trim(), lg.trim()
        )
    }
}

data class OnlineDoctorData( // 3 IDs PK
    val id: Long,
    val name: String,
    val line_id: Long,
    val account_id: Long,
    val type_id: Int,
    val active_date: String?,
    val inactive_date: String?,
    val speciality_id: Long,
    val class_id: Long,
    val gender: String,
    val target: Int?

    ) {
    fun toRoomDoctor(): Doctor {
        return Doctor(
            id, EmbeddedEntity(name.trim()), line_id, account_id, type_id, (active_date ?: "").trim(),
            (inactive_date ?: "").trim(), speciality_id, class_id, gender.trim(), target ?: 0
        )
    }
}


data class OnlinePresentationData(
    val id: Long,
    val name: String,
    val product_id: Long,
) {
    fun toRoomPresentation(): Presentation {
        return Presentation(id, EmbeddedEntity(name.trim()), product_id)
    }
}

data class OnlineSlideData(
    val id: Long,
    val slide_type: String,
    val slide_path: String,
    val presentation_id: Long,
    val thumbnail_id: Long,
    val thumbnail_path: String,
) {
    fun toRoomSlide(): Slide {
        return Slide(
            id, EmbeddedEntity("Slide Name!!"), slide_type.trim(), slide_path.trim(),
            presentation_id,  thumbnail_id, thumbnail_path.trim()
        )
    }
}

data class OnlinePlannedVisitData(
    val id: Long,
    val line_id: Long,
    val division_id: Long,
    val brick_id: Long,
    val type_id: Long,
    val account_id: Long,
    val doctor_id: Long?,
    val speciality_id: Long?,
    val acc_class: String?,
    val doc_class: String?,
    val visit_type_id: Int,
    val type: String,
    val date: String,
    val time: String
) {
    fun toRoomPlannedVisit(): PlannedVisit {
        return PlannedVisit(
            id, line_id, division_id, brick_id, type_id, account_id,
            doctor_id, speciality_id,
            if (acc_class != null && acc_class.trim().isNotEmpty()) acc_class.trim().toLong() else -1L,
            if (doc_class != null && doc_class.trim().isNotEmpty()) doc_class.trim().toLong() else -1L,
            if (visit_type_id == 1)
                MultiplicityEnum.SINGLE_VISIT
            else
                MultiplicityEnum.DOUBLE_VISIT,
            date.trim(), time.trim(),  false
        )
    }
}

data class OnlinePlannedOWData(
    val id: Long,
    val ow_type_id: Long,
    val date: String,
    val time: String,
    val shift_id: Int
) {
    fun toRoomPlannedOW(): PlannedOW {
        return PlannedOW(
            id, ow_type_id, shift_id, date.trim(), time.trim(), "", false
        )
    }
}

data class ActualVisitDTO(
    @SerializedName("visit_id") val visitId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("sync_date") val syncDate: String,
    @SerializedName("sync_time") val syncTime: String,
    @SerializedName("products") val products: List<Long>,
    @SerializedName("members") val members: List<Long>,
    @SerializedName("giveaways") val giveaways: List<Long>
)

data class OfficeWorkDTO(
    @SerializedName("ow_id") val owId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("sync_date") val syncDate: String,
    @SerializedName("sync_time") val syncTime: String
)

data class VacationDTO(
    @SerializedName("vacation_id") val vacationId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("sync_date") val syncDate: String,
    @SerializedName("sync_time") val syncTime: String
)

data class NewPlanDTO(
    @SerializedName("planned_id") val plannedId: Long,
    @SerializedName("offline_id") val offlineId: Long,
)

data class OfflineRecordDTO(
    @SerializedName("online_id") val onlineId: Long,
    @SerializedName("offline_id") val offlineId: Long
)

data class OnlineConfigurationData(
    @SerializedName("id") val id: Long,
    @SerializedName("pin") val pin: Long,
    @SerializedName("name") val name: String,
    @SerializedName("system") val system: String,
    @SerializedName("api_path") var link: String,
) {
    fun adjustThePath() {
        if (this.link.endsWith("/api/")) {
            this.link = this.link.removeSuffix("/api/")
        }
        else if (this.link.endsWith("/api")) {
            this.link = this.link.removeSuffix("/api")
        }
    }
}