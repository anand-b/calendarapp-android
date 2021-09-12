package com.nandboolean.calendarapp.viewmodel.event

import com.nandboolean.calendarapp.viewmodel.base.ViewStatus

data class EventViewState(
    val state: ViewStatus,
    val events: List<NBEventInstance> = emptyList(),
    val problemMessage: String? = null
)
