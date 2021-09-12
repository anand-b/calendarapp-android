package com.nandboolean.calendarapp.data.event.local

import android.content.ContentResolver
import android.provider.CalendarContract
import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.data.event.EventDataSource
import com.nandboolean.calendarapp.data.event.model.*

class EventProviderDataSource(
    private val contentResolver: ContentResolver
): EventDataSource {

    override fun getChronologicalEvents(timeRangeMs: LongRange): DataResult<List<EventInstanceData>> {
        val instances = when (val instancesResult = getEventInstances(timeRangeMs.first, timeRangeMs.last)) {
            // sort in chronological order
            is DataResult.Success -> instancesResult.data.sortedBy { it.startMs }
            is DataResult.Error -> throw instancesResult.error
            is DataResult.ProceedWithWarning -> {
                return DataResult.ProceedWithWarning(
                    emptyList(),
                    instancesResult.warning
                )
            }
        }
        return getEvents(instances)
    }

    private fun getEvents(
        instances: List<LocalEventInstanceData>
    ): DataResult<List<EventInstanceData>> {
        val uniqueEventIds = instances.map { it.eventId }.toSet()
        contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            EVENT_PROJECTION,
            "${CalendarContract.Events._ID} IN (?)",
            arrayOf(
                uniqueEventIds.joinToString(separator = " ") { "$it" }
            ),
            null
        )?.run {
            val eventsMap = mutableMapOf<Long, EventData>().apply {
                while (moveToNext()) {
                    try {
                        val id = getLong(EVENT_ID_IDX)
                        id to EventData(
                            id = id,
                            calendarId = getLong(CALENDAR_ID_IDX),
                            organizerEmail = getString(ORGANIZER_IDX),
                            title = getString(TITLE_IDX),
                            description = getString(DESCRIPTION_IDX),
                            isAllDay = getInt(ALL_DAY_IDX) == 1,
                            ownerRSVPStatus = rsvpStatus(getInt(OWNER_RSVP_IDX)),
                            availability = availability(getInt(AVAILABILITY_IDX)),
                            status = status(getInt(STATUS_IDX))
                        )
                    }
                    catch (exception: Throwable) {
                        close()
                        return DataResult.Error(exception)
                    }
                }
                close()
            }
            return DataResult.Success(instances.mapNotNull {
                eventsMap[it.eventId]?.run {
                    EventInstanceData(
                        eventData = this,
                        startTimeMs = it.startMs,
                        endTimeMs = it.endMs
                    )
                }
            })
        }
        return DataResult.ProceedWithWarning(
            emptyList(), "Could not query event details for event instances"
        )
    }

    private fun getEventInstances(fromMs: Long, toMs: Long): DataResult<List<LocalEventInstanceData>> {
        CalendarContract.Instances.query(
            contentResolver, EVENT_INSTANCES_PROJECTION, fromMs, toMs
        )?.run {
            val events = mutableListOf<LocalEventInstanceData>().apply {
                while (moveToNext()) {
                    try {
                        val eventInstanceData = LocalEventInstanceData(
                            getLong(EVENT_ID_IDX), getLong(START_MS_IDX), getLong(END_MS_IDX)
                        )
                        add(eventInstanceData)
                    } catch (exception: Throwable) {
                        close()
                        return DataResult.Error(exception)
                    }
                }
                close()
            }
            return DataResult.Success(events)
        }
        return DataResult.ProceedWithWarning(
            emptyList(),
            "Could not retrieve event instances in time range $fromMs to $toMs"
        )
    }

    private data class LocalEventInstanceData(
        val eventId: Long,
        val startMs: Long,
        val endMs: Long
    )

    private fun availability(flag: Int) = when(flag) {
        0 -> Availability.BUSY
        1 -> Availability.FREE
        2 -> Availability.TENTATIVE
        else -> throw IllegalStateException("Invalid event availability $flag")
    }

    private fun status(flag: Int) = when (flag) {
        0 -> Status.TENTATIVE
        1 -> Status.CONFIRMED
        2 -> Status.CANCELLED
        else -> throw IllegalStateException("Invalid event status $flag")
    }

    private fun rsvpStatus(flag: Int) = when (flag) {
        0 -> RSVPStatus.NONE
        1 -> RSVPStatus.ACCEPTED
        2 -> RSVPStatus.DECLINED
        3 -> RSVPStatus.INVITED
        4 -> RSVPStatus.TENTATIVE
        else -> throw IllegalStateException("Invalid owner RSVP: $flag")
    }

    companion object {
        private const val EVENT_ID_IDX = 0
        private const val START_MS_IDX = 1
        private const val END_MS_IDX = 2

        private val EVENT_INSTANCES_PROJECTION = arrayOf(
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END
        )

        private const val CALENDAR_ID_IDX = 1
        private const val ORGANIZER_IDX = 2
        private const val TITLE_IDX = 3
        private const val DESCRIPTION_IDX = 4
        private const val ALL_DAY_IDX = 5
        private const val OWNER_RSVP_IDX = 6
        private const val AVAILABILITY_IDX = 7
        private const val STATUS_IDX = 8

        private val EVENT_PROJECTION = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.ORGANIZER,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.SELF_ATTENDEE_STATUS,
            CalendarContract.Events.AVAILABILITY,
            CalendarContract.Events.STATUS
        )
    }
}