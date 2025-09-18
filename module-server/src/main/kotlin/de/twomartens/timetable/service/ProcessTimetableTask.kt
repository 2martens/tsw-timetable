package de.twomartens.timetable.service

import de.twomartens.timetable.model.db.Timetable
import de.twomartens.timetable.model.dto.TimetableState
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.scheduling.annotation.Async

@Async
class ProcessTimetableTask(
        private val asyncJobLauncher: JobLauncher,
        private val processTimetableJob: Job,
        private val tswTimetable: Timetable,
        private val repository: ScheduledProcessTimetableTaskRepository,
        private val scheduledTask: ScheduledProcessTimetableTask) : Runnable {

    override fun run() {
        processTimetable()
        repository.delete(scheduledTask)
    }

    fun processTimetable() {
        if (tswTimetable.timetableState != TimetableState.TIMETABLES_FETCHED) {
            log.info("Timetable ${tswTimetable.timetableId} is not in TIMETABLES_FETCHED state")
            return
        }

        val jobParameters = JobParametersBuilder()
                .addString("timetableId", tswTimetable.timetableId.value)
                .addString("userId", tswTimetable.userId.value)
                .toJobParameters()

        try {
            asyncJobLauncher.run(processTimetableJob, jobParameters)
        } catch (_: JobExecutionAlreadyRunningException) {
            log.info("Job is already running")
        } catch (_: JobInstanceAlreadyExistsException) {
            log.info("Job instance already exists")
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}