package de.twomartens.support.configuration

import de.twomartens.support.interceptor.HeaderInterceptorRest
import de.twomartens.support.interceptor.LoggingInterceptorRest
import de.twomartens.support.property.RestTemplateTimeoutProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class RestTemplateConfiguration {
    @Bean("restTemplate")
    open fun restTemplate(
            headerInterceptorRest: HeaderInterceptorRest,
            loggingInterceptor: LoggingInterceptorRest,
            restTemplateTimeoutProperties: RestTemplateTimeoutProperties
    ): RestTemplate {
        return RestTemplateBuilder()
                .additionalInterceptors(headerInterceptorRest, loggingInterceptor)
                .setConnectTimeout(restTemplateTimeoutProperties.connectionRestTemplateTimeoutInMillis)
                .setReadTimeout(restTemplateTimeoutProperties.readTimeoutRestTemplateInMillis)
                .build()
    }

    @Bean("restTemplateRestHealthIndicator")
    open fun restTemplateRestHealthIndicator(
            headerInterceptorRest: HeaderInterceptorRest,
            restTemplateTimeoutProperties: RestTemplateTimeoutProperties
    ): RestTemplate {
        return RestTemplateBuilder()
                .additionalInterceptors(headerInterceptorRest)
                .setConnectTimeout(restTemplateTimeoutProperties.connectionRestHealthIndicatorTimeoutInMillis)
                .setReadTimeout(restTemplateTimeoutProperties.readTimeoutRestHealthIndicatorInMillis)
                .build()
    }
}