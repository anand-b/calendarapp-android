package com.nandboolean.calendarapp.data.event.model

data class EventInstanceData(
    val eventData: EventData,
    val startTimeMs: Long,
    val endTimeMs: Long
)
