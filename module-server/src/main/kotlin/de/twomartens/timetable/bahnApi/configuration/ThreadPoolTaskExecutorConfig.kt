package de.twomartens.timetable.bahnApi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

private const val CORE_POOL_SIZE = 1

@Configuration
open class ThreadPoolTaskExecutorConfig {

    @Bean
    open fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = CORE_POOL_SIZE
        executor.maxPoolSize = CORE_POOL_SIZE
        return executor
    }
}