package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums

enum class IdAndNameTablesNamesEnum(private val _text: String) {
    // for saving data
    // LINE("LINE"),  the line table is split into a separate table
    SPECIALITY("SPECIALITY"), GIVEAWAY("GIVEAWAY"),
    OFFICE_WORK_TYPE("OFFICE_WORK_TYPE"), PRODUCT("PRODUCT"), MANAGER("MANAGER"),
    FEEDBACK("FEEDBACK"), CLASS("CLASS"),

    // for ui only
    /* for multiplicity choose in actual activity UI */ MULTIPLICITY("MULTIPLICITY"),
    /* for product samples choose in actual activity UI */ PRODUCT_SAMPLE("PRODUCT_SAMPLE"),
    /* for giveaway samples choose in actual activity UI */ GIVEAWAY_SAMPLE("GIVEAWAY_SAMPLE"),
    /* for number of doctor choose in actual activity UI */ NO_OF_DOCTORS("NO_OF_DOCTORS"),
    /* for shift choose in ow actual activity UI */ SHIFT("SHIFT"),
    /* for shift choose in vacation actual activity UI */ DURATION("DURATION"),
    /* for quotation payment method choose in actual activity UI */ QUOTATION_PAYMENT_METHOD("QUOTATION_PAYMENT_METHOD"),
    /* for coverage choose in planning activity UI */ COVERAGE("COVERAGE"),
    /* for un-selected choose in different activities UI */ UN_SELECTED("UN_SELECTED");

    val text: String get() = _text
}