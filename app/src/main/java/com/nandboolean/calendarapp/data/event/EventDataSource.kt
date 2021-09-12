package com.nandboolean.calendarapp.data.event

import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.data.event.model.EventInstanceData

interface EventDataSource {

    /**
     * Fetches calendar event instances in the given time range in chronological order.
     *
     * @param timeRangeMs range of time in milliseconds since epoch to fetch events for
     *
     * @return [DataResult.Success] containing a [List] of [EventInstanceData] on successful fetch,
     * or [DataResult.ProceedWithWarning] containing an empty [List] with a warning [String] when
     * there are warnings, or [DataResult.Error] containing an error [String].
     */
    fun getChronologicalEvents(timeRangeMs: LongRange): DataResult<List<EventInstanceData>>
}