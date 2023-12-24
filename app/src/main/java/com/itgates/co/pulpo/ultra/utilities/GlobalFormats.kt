package com.itgates.co.pulpo.ultra.utilities

import java.text.SimpleDateFormat
import java.util.*

object GlobalFormats {
    const val fullDateFormat = "yyyy-MM-dd HH:mm:ss a"
    const val dashDateFormat = "yyyy-MM-dd"
    const val dashDateFormat_YearMonth = "yyyy-MM"
    const val slashDateFormat = "yyyy/MM/dd"
    const val fullTimeFormat = "HH:mm:ss"
    const val partialTimeFormat = "HH:mm"

    fun getFullDate(local: Locale, date: Date): String {
        return Utilities.convertToEnglishDigits(SimpleDateFormat(this.fullDateFormat, local).format(date))
    }

    fun getDashedDate(local: Locale, date: Date): String {
        return Utilities.convertToEnglishDigits(SimpleDateFormat(this.dashDateFormat, local).format(date))
    }

    fun getDashedDateWithoutDayForDB(local: Locale, date: Date): String {
        return Utilities
            .convertToEnglishDigits(SimpleDateFormat(this.dashDateFormat_YearMonth, local).format(date))
            .plus("___")
    }

    fun getFullTime(local: Locale, date: Date): String {
        return Utilities.convertToEnglishDigits(SimpleDateFormat(this.fullTimeFormat, local).format(date))
    }
    fun getPartialTime(local: Locale, date: Date): String {
        return Utilities.convertToEnglishDigits(SimpleDateFormat(this.partialTimeFormat, local).format(date))
    }
}