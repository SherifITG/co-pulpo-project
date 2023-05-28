package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums

enum class SettingEnum(private val _text: String) {
    FIELD_NO_OF_DOCTORS("field_no_of_doctors"),
    METERS_TO_ACCEPT_DEVIATION("meters_to_accept_deviation"),
    NO_OF_GIVEAWAYS("no_of_giveaways"),
    NO_OF_PRODUCT("no_of_products"),
    IS_REQUIRED_PRODUCT_FEEDBACK("is_required_product_feedback"),
    IS_REQUIRED_PRODUCT_COMMENT("is_required_product_comment"),
    IS_REQUIRED_PRODUCT_FOLLOW_UP("is_required_product_followup"),
    IS_REQUIRED_PRODUCT_M_FEEDBACK("is_required_product_mfeedback");

    val text: String get() = _text
}