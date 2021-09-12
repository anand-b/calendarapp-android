package com.nandboolean.calendarapp.data.calendar.local

import android.content.ContentResolver
import android.provider.CalendarContract
import com.nandboolean.calendarapp.data.calendar.CalendarDataSource
import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.data.calendar.model.CalendarData

class CalendarProviderDataSource(
    private val contentResolver: ContentResolver
): CalendarDataSource {

    companion object {
        private val FIELDS = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.OWNER_ACCOUNT,
        )

        private const val ID_FIELD_IDX = 0
        private const val DISPLAY_NAME_FIELD_IDX = 1
        private const val ACCOUNT_NAME_FIELD_IDX = 2
        private const val ACCOUNT_TYPE_FIELD_IDX = 3
        private const val OWNER_ACCOUNT_FIELD_IDX = 4
    }

    override fun getCalendars(): DataResult<List<CalendarData>> {
        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            FIELDS, null, null, null
        )?.run {
            val calendars = mutableListOf<CalendarData>().apply {
                while (moveToNext()) {
                    try {
                        val calendarData = CalendarData(
                            getLong(ID_FIELD_IDX), getString(DISPLAY_NAME_FIELD_IDX),
                            getString(ACCOUNT_NAME_FIELD_IDX), getString(ACCOUNT_TYPE_FIELD_IDX),
                            getString(OWNER_ACCOUNT_FIELD_IDX)
                        )
                        add(calendarData)
                    }
                    catch (exception: Throwable) {
                        close()
                        return DataResult.Error(exception)
                    }
                }
                close()
            }
            return DataResult.Success(calendars)
        }
        return DataResult.ProceedWithWarning(
            emptyList(), "Could not query for calendars; falling back to empty list."
        )
    }
}