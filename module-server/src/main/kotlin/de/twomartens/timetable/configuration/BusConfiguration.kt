package de.twomartens.timetable.configuration

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan
import org.springframework.context.annotation.Configuration

@Configuration
@RemoteApplicationEventScan(basePackages = ["de.twomartens.timetable", "de.twomartens.support"])
open class BusConfiguration
