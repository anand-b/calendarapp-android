package com.nandboolean.calendarapp.data.event.model

data class EventData(
    val id: Long,
    val calendarId: Long,
    val organizerEmail: String,
    val title: String,
    val description: String = "",
    val startTime: Long = -1,
    val endTime: Long = -1,
    val isAllDay: Boolean,

    private val ownerRSVPStatus: RSVPStatus,
    // indicates if this event is a busy event or free event
    private val availability: Availability,
    // indicates if the event is tentative, confirmed or cancelled
    private val status: Status
)
