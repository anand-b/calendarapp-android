package com.nandboolean.calendarapp.viewmodel.calendar

import com.nandboolean.calendarapp.data.calendar.model.CalendarData

data class NBCalendar(
    val calendarData: CalendarData,
    val isEnabled: Boolean = true
)
