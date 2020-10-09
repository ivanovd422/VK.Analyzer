package com.lab422.vkanalyzer.utils.extensions

import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.misc.TimeInterval
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.uiFormatted(datePattern: String, stringProvider: StringProvider): String {
    val months = stringProvider.stringArrayById(R.array.months)
    val locale = Locale("ru")
    val dfs = DateFormatSymbols.getInstance(locale)
    dfs.shortMonths = months
    val format = SimpleDateFormat(datePattern, locale)
    format.timeZone = TimeZone.getTimeZone("UTC")
    format.dateFormatSymbols = dfs
    return format.format(this)
}

val Date.daysFromToday: Long
    get() {
        return daysFrom(Date().beginningOfDay)
    }

fun Date.daysFrom(date: Date): Long {
    val millsInSec = 1000.0
    return Math.ceil((this.time - date.beginningOfDay.time) / (millsInSec * TimeInterval.day)).toLong()
}

val Date.beginningOfDay: Date
    get() {
        val calendar = nowCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

fun Date.nowCalendar(): Calendar{
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}