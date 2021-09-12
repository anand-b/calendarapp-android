package com.nandboolean.calendarapp.viewmodel.calendar

import com.nandboolean.calendarapp.viewmodel.base.ViewStatus

data class CalendarViewState(
    val state: ViewStatus,
    val calendars: List<NBCalendar> = emptyList(),
    val problemMessage: String? = null
)
