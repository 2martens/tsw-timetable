package de.twomartens.timetable.model.dto

enum class TimetableState {
    NEW,
    FETCHING_TIMETABLES,
    TIMETABLES_FETCHED,
    SERVICES_COLLECTED,
    ENTER_FORMATIONS,
    LINK_SERVICES,
    READY_FOR_USAGE
}