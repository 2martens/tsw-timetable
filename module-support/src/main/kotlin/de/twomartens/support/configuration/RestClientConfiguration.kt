package de.twomartens.support.configuration

import de.twomartens.support.interceptor.HeaderInterceptorRest
import de.twomartens.support.interceptor.LoggingInterceptorRest
import de.twomartens.support.property.RestTemplateTimeoutProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter
import org.springframework.web.client.RestClient

@Configuration
open class RestClientConfiguration {
    @Bean("restClient")
    open fun restClient(
            headerInterceptorRest: HeaderInterceptorRest,
            loggingInterceptor: LoggingInterceptorRest,
            restTemplateTimeoutProperties: RestTemplateTimeoutProperties
    ): RestClient {
        return RestClient.builder()
                .messageConverters { it.add(Jaxb2RootElementHttpMessageConverter()) }
                .requestInterceptors { listOf(headerInterceptorRest, loggingInterceptor) }
                .build()
    }
}