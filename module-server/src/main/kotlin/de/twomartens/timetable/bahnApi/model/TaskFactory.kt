package de.twomartens.timetable.bahnApi.model

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.model.base.HourAtDay
import org.springframework.stereotype.Component
import java.time.LocalDateTime

private const val SECONDS_BETWEEN_TASKS = 2

private const val ZERO = 0

@Component
class TaskFactory {

    private val _tasksPerHour: MutableMap<HourAtDay, Int> = mutableMapOf()

    fun initializeWithExistingTasks(existingTasks: List<ScheduledFetchTask>) {
        resetCounter()
        countTasks(existingTasks)
    }

    fun createTaskAndUpdateCounter(eva: Eva, hourAtDay: HourAtDay): ScheduledFetchTask {
        val fetchDateTime = calculateNextAvailableFetchDateTime(hourAtDay)
        incrementTaskCountForHourAtDay(hourAtDay)
        return ScheduledFetchTask(eva, hourAtDay, fetchDateTime)
    }

    private fun calculateNextAvailableFetchDateTime(hourAtDay: HourAtDay): LocalDateTime {
        val secondInHourToExecute = calculateSecondInHourToCalculate(hourAtDay)
        return calculateFetchDateTime(hourAtDay, secondInHourToExecute)
    }

    private fun calculateSecondInHourToCalculate(hourAtDay: HourAtDay): Int {
        val numberOfTasksInHour = getNumberOfTasksInHour(hourAtDay)
        return numberOfTasksInHour * SECONDS_BETWEEN_TASKS
    }

    private fun calculateFetchDateTime(
            hourAtDay: HourAtDay,
            secondInHourToExecute: Int
    ): LocalDateTime {
        return hourAtDay.dateTime.plusSeconds(secondInHourToExecute.toLong())
    }

    private fun countTasks(existingTasks: List<ScheduledFetchTask>) {
        existingTasks.forEach { countTask(it) }
    }

    private fun countTask(scheduledFetchTask: ScheduledFetchTask) {
        val scheduledTime = scheduledFetchTask.scheduledExecutionDateTime
        val hourAtDay = HourAtDay.of(scheduledTime)
        incrementTaskCountForHourAtDay(hourAtDay)
    }

    private fun incrementTaskCountForHourAtDay(hourAtDay: HourAtDay) {
        val numberOfTasksInHour = getNumberOfTasksInHour(hourAtDay)
        _tasksPerHour[hourAtDay] = numberOfTasksInHour + 1
    }

    private fun getNumberOfTasksInHour(hourAtDay: HourAtDay) =
            _tasksPerHour.computeIfAbsent(hourAtDay) { ZERO }

    private fun resetCounter() {
        _tasksPerHour.clear()
    }
}
