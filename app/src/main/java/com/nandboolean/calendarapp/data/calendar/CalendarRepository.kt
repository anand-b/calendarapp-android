package com.nandboolean.calendarapp.data.calendar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalendarRepository(private val dataSource: CalendarDataSource) {

    /**
     * Fetches a list of calendars from the provided data source
     */
    suspend fun get() = withContext(Dispatchers.IO) {
        dataSource.getCalendars()
    }
}