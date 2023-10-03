package de.twomartens.timetable.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

private const val POOL_SIZE = 1

private const val THREAD_NAME_PREFIX = "ThreadPoolTaskScheduler"

@Configuration
open class ThreadPoolTaskSchedulerConfig {
    @Bean
    open fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = POOL_SIZE
        scheduler.threadNamePrefix = THREAD_NAME_PREFIX
        return scheduler
    }
}