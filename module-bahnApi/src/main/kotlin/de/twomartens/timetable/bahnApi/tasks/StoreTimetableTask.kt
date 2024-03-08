package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async

@Async
open class StoreTimetableTask(
        private val timetable: BahnTimetable,
        private val userId: UserId,
        private val routeId: RouteId,
        private val hourAtDay: HourAtDay,
        private val bahnDatabaseService: BahnDatabaseService
) : Runnable {
    override fun run() {
        log.info { "Store timetable: [eva: ${timetable.eva}], [station: ${timetable.station}], [hour: ${hourAtDay}]" }
        bahnDatabaseService.storeTimetable(timetable, userId, routeId, hourAtDay)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}