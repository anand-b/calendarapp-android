package com.nandboolean.calendarapp.viewmodel.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.data.event.EventRepository
import com.nandboolean.calendarapp.viewmodel.base.AbsBaseViewModel
import com.nandboolean.calendarapp.viewmodel.base.ViewStatus
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository
) : AbsBaseViewModel() {

    val events: LiveData<EventViewState>
        get() = _events
    private val _events: MutableLiveData<EventViewState> = MutableLiveData()

    fun fetchEventRange(fromMs: Long, toMsInclusive: Long) {
        viewModelScope.launch {
            _events.value = when(val eventResult = repository.get(fromMs, toMsInclusive)) {
                is DataResult.Success -> EventViewState(
                    state = ViewStatus.SUCCESS,
                    events = eventResult.data.map { NBEventInstance(it) }
                )

                is DataResult.Error -> EventViewState(
                    state = ViewStatus.ERROR,
                    problemMessage = eventResult.error.localizedMessage
                )

                is DataResult.ProceedWithWarning -> EventViewState(
                    state = ViewStatus.WARNING,
                    events = eventResult.data.map { NBEventInstance(it) },
                    problemMessage = eventResult.warning
                )
            }
        }
    }

}