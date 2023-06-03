package com.itgates.ultra.pulpo.cira.network.models.responseModels.responses

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Account
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.PlannedVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.google.gson.annotations.SerializedName
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import kotlin.streams.toList

data class UserDetailsData(
    val UserId: String,
    val DefaultLineName: String,
    val Code: String,
    val Name: String,
    val NameAr: String,
    val NameEn: String,
    val Mobile: String,
    val Username: String,
    val Password: String,
    val Pic: String,
    val PicCrm: String,
    val Active: String,
    val TypeId: String,
    val DivisionName: String,
    val HireDate: String,
    val DivIds: String,
    val ProductId: String?,
    val IsManager: String,
    val LineIds: String,
    val PositionId: String?,
    val DefaultLineId: String,
    val LevelId: String,
    val KOLList: String
)

data class OnlineMasterData(
    @SerializedName("account_types") val accTypes: ArrayList<OnlineAccountTypeData>,
    @SerializedName("lines") val lines: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("specialties") val specialties: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("divisions") val divisions: ArrayList<OnlineDivisionData>,
    @SerializedName("giveaways") val giveaways: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("office_work_types") val officeWorkTypes: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("bricks") val bricks: ArrayList<OnlineBrickData>,
    @SerializedName("managers") val managers: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("classes") val classes: ArrayList<OnlineClassData>,
    @SerializedName("settings") val settings: ArrayList<OnlineSettingData>,
    @SerializedName("products") val products: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("forms") val forms: ArrayList<OnlineFormData>,
    @SerializedName("actions") val actions: ArrayList<OnlineActionData>,
    @SerializedName("comments") val comments: ArrayList<OnlineIdAndNameObjectData>,
) {
    constructor() : this(
        ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(),
        ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(),
    )

    fun collectAllIdAndNameRoomObjects(): List<IdAndNameEntity> {
        return lines.stream().map { it.toRoomLine() }.toList()
                .asSequence()
                .plus(specialties.stream().map { it.toRoomSpeciality() }.toList())
                .plus(giveaways.stream().map { it.toRoomGiveaway() }.toList())
                .plus(officeWorkTypes.stream().map { it.toRoomOfficeWorkTypes() }.toList())
                .plus(managers.stream().map { it.toRoomManager() }.toList())
                .plus(products.stream().map { it.toRoomProduct() }.toList())
                .plus(comments.stream().map { it.toRoomComment() }.toList())
                .plus(forms.stream().map { it.toRoomForm() }.toList())
                .plus(actions.stream().map { it.toRoomAction() }.toList())
                .toList()
    }

    fun collectAccTypeRoomObjects(): List<AccountType> {
        return accTypes.stream().map { it.toRoomAccType() }.toList()
    }

    fun collectBrickRoomObjects(): List<Brick> {
        return bricks.stream().map { it.toRoomBrick() }.toList()
    }

    fun collectClassRoomObjects(): List<Class> {
        return classes.stream().map { it.toRoomClass() }.toList()
    }

    fun collectDivisionRoomObjects(): List<Division> {
        return divisions.stream().map { it.toRoomDivision() }.toList()
    }

    fun collectSettingRoomObjects(): List<Setting> {
        return settings.stream().map { it.toRoomSetting() }.toList()
    }
}

data class OnlineAccountsAndDoctorsData(
    @SerializedName("Accounts") val accounts: ArrayList<OnlineAccountData>,
    @SerializedName("Doctors") val doctors: ArrayList<OnlineDoctorData>
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
    @SerializedName("Presentations") val Presentations: ArrayList<OnlinePresentationData>,
    @SerializedName("Slides") val Slides: ArrayList<OnlineSlideData>
) {
    constructor() : this(ArrayList(), ArrayList())

    fun collectPresentationRoomObjects(): List<Presentation> {
        return Presentations.stream().map { it.toRoomPresentation() }.toList()
    }

    fun collectSlideRoomObjects(): List<Slide> {
        return Slides.stream().map { it.toRoomSlide() }.toList()
    }
}

data class OnlineAccountTypeData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("tbl") val tbl: String,
    @SerializedName("shortcut") val shortcut: String,
    @SerializedName("sorting") val sorting: String,
    @SerializedName("cat_id") val cat_id: Int,
) {
    fun toRoomAccType(): AccountType = AccountType(
        this.id,
        EmbeddedEntity(this.name),
        this.tbl,
        this.shortcut,
        this.sorting,
        this.cat_id
    )
}

data class OnlineDivisionData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("team_id") val team_id: String,
    @SerializedName("parent_id") val parent_id: String,
    @SerializedName("type_id") val type_id: String,
    @SerializedName("date_from") val date_from: String,
    @SerializedName("date_to") val date_to: String,
    @SerializedName("sorting") val sorting: String,
    @SerializedName("related_id") val related_id: String,
) {
    fun toRoomDivision(): Division = Division(
        this.id,
        EmbeddedEntity(this.name),
        this.team_id,
        this.parent_id,
        this.type_id,
        this.date_from,
        this.date_to,
        this.sorting,
        this.related_id,
    )
}

data class OnlineBrickData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("team_id") val team_id: String,
    @SerializedName("ter_id") val ter_id: String
) {
    fun toRoomBrick(): Brick = Brick(this.id, EmbeddedEntity(this.name), this.team_id, this.ter_id)
}

data class OnlineClassData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cat_id") val cat_id: Long,
){
    fun toRoomClass(): Class = Class(this.id, EmbeddedEntity(this.name), this.cat_id)
}

data class OnlineSettingData(
    @SerializedName("id") val id: Long,
    @SerializedName("attribute_name") val attribute_name: String,
    @SerializedName("attribute_value") val attribute_value: String,
) {
    fun toRoomSetting(): Setting = Setting(this.id, EmbeddedEntity(this.attribute_name), this.attribute_value)
}

data class OnlineFormData(
    @SerializedName("id") val id: Long,
    @SerializedName("form") val form: String,
) {
    fun toRoomForm(): IdAndNameEntity = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.FORM, EmbeddedEntity(this.form))
}

data class OnlineActionData(
    @SerializedName("id") val id: Long,
    @SerializedName("action") val action: String,
) {
    fun toRoomAction(): IdAndNameEntity = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.ACTION, EmbeddedEntity(this.action))
}

// include classes such as Line & Speciality & Giveaway & OfficeWorkTypes & Product & Manager & Comment & Form & Action
data class OnlineIdAndNameObjectData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
) {
    fun toRoomLine(): IdAndNameEntity = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.LINE, EmbeddedEntity(this.name))
    fun toRoomSpeciality() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.SPECIALITY, EmbeddedEntity(this.name))
    fun toRoomGiveaway() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.GIVEAWAY, EmbeddedEntity(this.name))
    fun toRoomOfficeWorkTypes() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE, EmbeddedEntity(this.name))
    fun toRoomProduct() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.PRODUCT, EmbeddedEntity(this.name))
    fun toRoomManager() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.MANAGER, EmbeddedEntity(this.name))
    fun toRoomComment() = IdAndNameEntity(this.id, IdAndNameTablesNamesEnum.COMMENT, EmbeddedEntity(this.name))
}

data class OnlineAccountData(
    val id: Long,
    val t_team_id: Long,
    val t_div_id: Long,
    val t_class_id: Long,
    val team_ll: String,
    val team_lg: String,
    val ref_id: Long,
    val name: String,
    val brick_id: Long,
    val address: String,
    val tel: String,
    val mobile: String,
    val tbl: String,
) {
    fun toRoomAccount(): Account {
        return Account(
            id, EmbeddedEntity(name), t_team_id, t_div_id, t_class_id,
            team_ll, team_lg, ref_id, brick_id, address, tel, mobile, tbl
        )
    }
}

data class OnlineDoctorData(
    val id: Long,
    val doc_acc_id: Long,
    val d_account_id: Long,
    val d_active_from: String,
    val d_inactive_from: String,
    val team_id: Long,
    val name: String,
    val specialization_id: Long,
    val class_id: Long,
    val active_from: String,
    val inactive_from: String,
    val aactive: Int,
    val tbl: String,
) {
    fun toRoomDoctor(): Doctor {
        return Doctor(
            id, EmbeddedEntity(name), doc_acc_id, d_account_id, d_active_from, d_inactive_from,
            team_id, specialization_id, class_id, active_from, inactive_from, aactive, tbl
        )
    }
}

data class OnlinePresentationData(
    val id: Long,
    val name: String,
    val description: String,
    val insert_date: String,
    val insert_time: String,
    val active: Int,
    val product_id: Long,
    val brand_id: Long,
    val team_id: Long,
    val Product: String,
    val structure: String,
) {
    fun toRoomPresentation(): Presentation {
        return Presentation(
            id, EmbeddedEntity(name), description, active, product_id, team_id, Product, structure
        )
    }
}

data class OnlineSlideData(
    val id: Long,
    val title: String,
    val description: String,
    val contents: String,
    val presentation_id: Long,
    val product_id: Long,
    val brand_id: Long,
    val slide_type: String,
    val file_path: String,
    val structure: String,
) {
    fun toRoomSlide(): Slide {
        return Slide(
            id, EmbeddedEntity(title), description, contents, presentation_id, product_id, slide_type, file_path, structure
        )
    }
}

data class OnlinePlannedVisitData(
    val id: Long,
    val div_id: Long,
    val account_type: Long,
    val item_id: Long,
    val item_doc_id: Long,
    val members: Long,
    val vdate: String,
    val vtime: String,
    val shift: Int,
    val insertion_date: String,
    val user_id: Long,
    val team_id: Long,
    val related_id: Long
) {
    fun toRoomPlannedVisit(): PlannedVisit {
        return PlannedVisit(
            id, div_id, account_type, item_id, item_doc_id, members, vdate, vtime,
            shift, insertion_date, user_id, team_id, false, related_id,
        )
    }
}

data class ActualVisitDTO(
    @SerializedName("visit_id") val visitId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("is_synced") val isSynced: Int,
    @SerializedName("sync_date") val syncDate: String,
    @SerializedName("sync_time") val syncTime: String,
    @SerializedName("products") val products: List<Long>,
    @SerializedName("members") val members: List<Long>,
    @SerializedName("giveaways") val giveaways: List<Long>
)

data class OfflineRecordDTO(
    @SerializedName("online_id") val onlineId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("is_synced") val isSynced: Int,
)