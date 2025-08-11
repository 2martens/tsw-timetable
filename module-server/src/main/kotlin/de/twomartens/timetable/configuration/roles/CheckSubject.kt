package de.twomartens.timetable.configuration.roles

import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
annotation class CheckSubject(
        val partyParamName: String    // Name of method argument holding the party ID
)