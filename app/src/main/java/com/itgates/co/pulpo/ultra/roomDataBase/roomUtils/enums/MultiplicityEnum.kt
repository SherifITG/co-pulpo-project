package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums

enum class MultiplicityEnum(private val _text: String) {
    SINGLE_VISIT("Single"), DOUBLE_VISIT("Double");

    val text: String get() = _text
}