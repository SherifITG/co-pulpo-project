package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums

object DivisionColumns {
    const val ID = "id"
    const val EMBEDDED_DIVISION_NAME = "embedded_division_name"
    const val TEAM_ID = "team_id"
    const val PARENT_ID = "parent_id"
    const val TYPE_ID = "type_id"
    const val DATE_FROM = "date_from"
    const val DATE_TO = "date_to"
    const val SORTING = "sorting"
    const val RELATED_ID = "related_id"
}

object BrickColumns {
    const val ID = "id"
    const val EMBEDDED_BRICK_NAME = "embedded_brick_"
    const val TEAM_ID = "team_id"
    const val TERRITORY_ID = "ter_id"
}

object AccountTypeColumns  {
    const val ID = "id"
    const val EMBEDDED_ACCOUNT_TYPE_NAME = "embedded_account_type_name"
    const val TBL = "tbl"
    const val SHORTCUT = "shortcut"
    const val SORTING = "sorting"
    const val CATEGORY_ID = "cat_id"
}

object ClassColumns  {
    const val ID = "id"
    const val EMBEDDED_CLASS_NAME = "embedded_class_name"
    const val CATEGORY_ID = "cat_id"
}

object SettingColumns  {
    const val ID = "id"
    const val EMBEDDED_SETTING_NAME = "embedded_setting_name"
    const val VALUE = "value"
}

object IdAndNameEntityColumns  {
    const val ID = "id"
    const val TABLE_IDENTIFIER = "table_identifier"
    const val EMBEDDED_ENTITY_NAME = "embedded_entity_name"
}

object AccountColumns  {
    const val ID = "id"
    const val EMBEDDED_ACCOUNT_NAME = "embedded_account_name"
    const val TEAM_ID = "team_id"
    const val DIVISION_ID = "division_id"
    const val CLASS_ID = "class_id"
    const val LL_FIRST = "ll_first"
    const val LG_FIRST = "lG_first"
    const val REFERENCE_ID = "reference_id"
    const val BRICK_ID = "brick_id"
    const val ADDRESS = "address"
    const val TELEPHONE = "telephone"
    const val MOBILE = "mobile"
    const val TBL = "tbl"
}

object DoctorColumns  {
    const val ID = "id"
    const val EMBEDDED_DOCTOR_NAME = "embedded_doctor_name"
    const val DOCTOR_ACCOUNT_ID = "doctor_account_id"
    const val ACCOUNT_ID = "account_id"
    const val D_ACTIVE_FROM = "d_active_from"
    const val D_INACTIVE_FROM = "d_inactive_from"
    const val TEAM_ID = "team_id"
    const val SPECIALIZATION_ID = "specialization_id"
    const val CLASS_ID = "class_id"
    const val ACTIVE_FROM = "active_from"
    const val INACTIVE_FROM = "inactive_from"
    const val ACTIVE = "active"
    const val TBL = "tbl"
}