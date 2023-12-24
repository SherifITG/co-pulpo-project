package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums

object DivisionColumns {
    const val ID = "id"
    const val EMBEDDED_DIVISION_NAME = "embedded_division_name"
    const val LINE_ID = "line_id"
    const val PARENT_ID = "parent_id"
    const val TYPE_ID = "type_id"
    const val DATE_FROM = "date_from"
    const val DATE_TO = "date_to"
    const val SORTING = "sorting"
    const val RELATED_ID = "related_id"
}

object BrickColumns {
    const val ID = "id"
    const val EMBEDDED_BRICK_NAME = "embedded_brick_name"
    const val LINE_ID = "line_id"
    const val TERRITORY_ID = "ter_id"
}

object AccountTypeColumns  {
    const val ID = "id"
    const val EMBEDDED_ACCOUNT_TYPE_NAME = "embedded_account_type_name"
    const val SORT = "sort"
    const val SHIFT_ID = "shift_id"
    const val ACCEPTED_DISTANCE = "accepted_distance"
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
    const val LINE_ID = "line_id"
    const val EMBEDDED_ENTITY_NAME = "embedded_entity_name"
}

object AccountColumns  {
    const val ID = "id"
    const val EMBEDDED_ACCOUNT_NAME = "embedded_account_name"
    const val LINE_ID = "line_id"
    const val DIVISION_ID = "division_id"
    const val CLASS_ID = "class_id"
    const val ACCOUNT_TYPE_ID = "acc_type_id"
    const val BRICK_ID = "brick_id"
    const val ADDRESS = "address"
    const val TELEPHONE = "telephone"
    const val MOBILE = "mobile"
    const val EMAIL = "email"
    const val LL_FIRST = "ll_first"
    const val LG_FIRST = "lG_first"
}

object DoctorColumns  {
    const val ID = "id"
    const val EMBEDDED_DOCTOR_NAME = "embedded_doctor_name"
    const val LINE_ID = "line_id"
    const val ACCOUNT_ID = "account_id"
    const val ACCOUNT_TYPE_ID = "acc_type_id"
    const val ACTIVE_DATE = "active_date"
    const val INACTIVE_DATE = "inactive_date"
    const val SPECIALIZATION_ID = "specialization_id"
    const val CLASS_ID = "class_id"
    const val GENDER = "gender"
    const val TARGET = "target"
}