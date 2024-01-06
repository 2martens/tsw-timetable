package de.twomartens.timetable

import de.twomartens.support.model.LeadershipStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.integration.leader.event.OnGrantedEvent
import org.springframework.integration.leader.event.OnRevokedEvent
import org.springframework.scheduling.annotation.EnableScheduling

@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication(scanBasePackages = ["de.twomartens.support", "de.twomartens.timetable"])
open class MainApplication(
        private val leadershipStatus: LeadershipStatus,
        private val leaderProperties: LeaderProperties
) {
    @EventListener(ApplicationReadyEvent::class)
    fun ready(event: ApplicationReadyEvent) {
        setInitialLeadershipStatus()
    }

    @EventListener(OnGrantedEvent::class)
    fun onLeadershipGranted(event: OnGrantedEvent) {
        leadershipStatus.isLeader = true
    }

    @EventListener(OnRevokedEvent::class)
    fun onLeadershipRevoked(event: OnRevokedEvent) {
        leadershipStatus.isLeader = false
    }

    private fun setInitialLeadershipStatus() {
        leadershipStatus.isLeader = !leaderProperties.isEnabled
    }
}

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

