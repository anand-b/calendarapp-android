package com.nandboolean.calendarapp.viewmodel.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandboolean.calendarapp.data.calendar.CalendarRepository
import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.viewmodel.base.AbsBaseViewModel
import com.nandboolean.calendarapp.viewmodel.base.ViewStatus
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val repository: CalendarRepository
): AbsBaseViewModel() {

    val calendars: LiveData<CalendarViewState>
        get() = _calendars
    private val _calendars: MutableLiveData<CalendarViewState> = MutableLiveData()

    fun fetchCalendars() {
        viewModelScope.launch {
            _calendars.value = when (val result = repository.get()) {
                is DataResult.Success -> CalendarViewState(
                    state = ViewStatus.SUCCESS,
                    calendars = result.data.map { NBCalendar(it, true) }
                )
                is DataResult.ProceedWithWarning -> CalendarViewState(
                    state = ViewStatus.WARNING,
                    calendars = result.data.map { NBCalendar(it, true) },
                    problemMessage = result.warning
                )
                is DataResult.Error -> CalendarViewState(
                    state = ViewStatus.ERROR,
                    problemMessage = result.error.localizedMessage
                )
            }
        }
    }
}