package de.twomartens.timetable.support.configuration

import de.twomartens.timetable.support.interceptor.HeaderInterceptorRest
import de.twomartens.timetable.support.interceptor.LoggingInterceptorRest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
open class InterceptorConfiguration {
  @Bean
  open fun loggingInterceptorRest(clock: Clock): LoggingInterceptorRest {
    return LoggingInterceptorRest(clock)
  }

  @Bean
  open fun headerInterceptorRest(): HeaderInterceptorRest {
    return HeaderInterceptorRest()
  }
}