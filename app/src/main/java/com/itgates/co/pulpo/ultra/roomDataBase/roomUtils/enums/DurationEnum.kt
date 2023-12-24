package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums

enum class DurationEnum(private val _text: String, private val _index: Byte) {
    FULL_DAY("Full Day", 1), HALF_DAY("Half Day", 2)
    ;

    val text: String get() = _text
    val index: Byte get() = _index
}