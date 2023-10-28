package de.twomartens.timetable.support.configuration

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan
import org.springframework.context.annotation.Configuration

@Configuration
@RemoteApplicationEventScan(basePackages = ["de.twomartens.timetable"])
open class BusConfiguration
