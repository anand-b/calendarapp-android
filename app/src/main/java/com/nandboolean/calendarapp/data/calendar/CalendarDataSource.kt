package com.nandboolean.calendarapp.data.calendar

import com.nandboolean.calendarapp.data.calendar.model.CalendarData
import com.nandboolean.calendarapp.data.common.DataResult

interface CalendarDataSource {

    fun getCalendars(): DataResult<List<CalendarData>>
}