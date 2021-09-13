package com.nandboolean.calendarapp.ui.schedule

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nandboolean.calendarapp.R
import com.nandboolean.calendarapp.data.calendar.model.CalendarData
import com.nandboolean.calendarapp.ui.base.BaseCalendarActivity
import com.nandboolean.calendarapp.viewmodel.calendar.CalendarViewModel
import com.nandboolean.calendarapp.viewmodel.calendar.CalendarViewState

/**
 * Displays the schedule on the user's calendar on the given selected date
 */
class ScheduleActivity : BaseCalendarActivity() {

    private val calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        calendarViewModel.calendars.observe(this) { calendars ->
            renderCalendars(calendars)
        }
    }

    private fun renderCalendars(calendars: CalendarViewState) {}
}