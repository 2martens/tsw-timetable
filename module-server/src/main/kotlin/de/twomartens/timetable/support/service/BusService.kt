package de.twomartens.timetable.support.service

import org.springframework.beans.factory.ObjectProvider
import org.springframework.cloud.bus.BusBridge
import org.springframework.cloud.bus.event.RemoteApplicationEvent
import org.springframework.stereotype.Service

@Service
class BusService(
        private val busBridge: ObjectProvider<BusBridge>
) {
    fun publishEvent(event: RemoteApplicationEvent) {
        busBridge.ifAvailable {
            it.send(event)
        }
    }
}