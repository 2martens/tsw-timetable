package de.twomartens.support.monitoring.actuator

import java.io.Closeable

fun interface Preparable {
    fun prepare(): Closeable
}
