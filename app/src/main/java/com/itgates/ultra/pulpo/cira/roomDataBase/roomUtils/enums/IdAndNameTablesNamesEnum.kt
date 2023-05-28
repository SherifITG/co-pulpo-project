package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums

enum class IdAndNameTablesNamesEnum(private val _text: String) {
    // for saving data
    LINE("LINE"), SPECIALITY("SPECIALITY"), GIVEAWAY("GIVEAWAY"),
    OFFICE_WORK_TYPE("OFFICE_WORK_TYPE"), PRODUCT("PRODUCT"), MANAGER("MANAGER"),
    COMMENT("COMMENT"), FORM("FORM"), ACTION("ACTION"),

    // for ui only
    /* for multiplicity choose in actual activity UI */ MULTIPLICITY("MULTIPLICITY"),
    /* for product samples choose in actual activity UI */ PRODUCT_SAMPLE("PRODUCT_SAMPLE"),
    /* for giveaway samples choose in actual activity UI */ GIVEAWAY_SAMPLE("GIVEAWAY_SAMPLE"),
    /* for giveaway samples choose in actual activity UI */ NO_OF_DOCTORS("NO_OF_DOCTORS"),
    /* for giveaway samples choose in actual activity UI */ SHIFT("SHIFT"),
    /* for un-selected choose in actual activity UI */ UN_SELECTED("UN_SELECTED");

    val text: String get() = _text
}