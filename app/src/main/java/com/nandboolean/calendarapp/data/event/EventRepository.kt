package com.nandboolean.calendarapp.data.event

import com.nandboolean.calendarapp.data.common.DataResult
import com.nandboolean.calendarapp.data.event.model.EventInstanceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val dataSource: EventDataSource) {

    /**
     * Gets a list of event instances in chronological order
     *
     * @param fromMs time in millis since epoch (inclusive)
     * @param toMsInclusive time in millis since epoch (inclusive)
     *
     * @return [DataResult]
     */
    suspend fun get(fromMs: Long, toMsInclusive: Long): DataResult<List<EventInstanceData>> =
        withContext(Dispatchers.IO) {
            dataSource.getChronologicalEvents(LongRange(fromMs, toMsInclusive))
        }

}