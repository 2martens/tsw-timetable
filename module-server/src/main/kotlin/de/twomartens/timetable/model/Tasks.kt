package de.twomartens.timetable.model

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val SECONDS_BETWEEN_TASKS = 2

class Tasks {
    private val _tasks: MutableMap<HourAtDay, MutableList<ScheduledFetchTask>> = mutableMapOf()
    var tasks: List<ScheduledFetchTask> = listOf()
        get() {
            return _tasks.flatMap {
                it.value
            }
        }
        set(newTasks) {
            _tasks.clear()
            newTasks.forEach { addTask(it) }
            field = newTasks
        }

    fun createAndAddTask(eva: Eva, date: LocalDate, hour: Hour): ScheduledFetchTask {
        val hourAtDay = HourAtDay(date.atTime(hour.value, 0))
        val tasksForHourAtDay = _tasks.computeIfAbsent(hourAtDay) { mutableListOf() }
        val secondInHourToExecute = tasksForHourAtDay.size * SECONDS_BETWEEN_TASKS
        val fetchDateTime = hourAtDay.value.plusSeconds(secondInHourToExecute.toLong())
        val task = ScheduledFetchTask(eva, date, hour, fetchDateTime)

        tasksForHourAtDay.add(task)

        return task
    }

    private fun addTask(scheduledFetchTask: ScheduledFetchTask) {
        val scheduledTime = scheduledFetchTask.scheduledExecutionDateTime
        val hourAtDay = HourAtDay(scheduledTime.truncatedTo(ChronoUnit.HOURS))
        val tasksForHourAtDay = _tasks.computeIfAbsent(hourAtDay) { mutableListOf() }

        tasksForHourAtDay.add(scheduledFetchTask)
    }
}