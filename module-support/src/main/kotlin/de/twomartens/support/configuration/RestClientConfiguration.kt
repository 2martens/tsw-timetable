package de.twomartens.support.configuration

import de.twomartens.support.interceptor.HeaderInterceptorRest
import de.twomartens.support.interceptor.LoggingInterceptorRest
import de.twomartens.support.property.RestTemplateTimeoutProperties
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
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

    @Bean("restClientRestHealthIndicator")
    open fun restClientRestHealthIndicator(
            headerInterceptorRest: HeaderInterceptorRest,
            restTemplateTimeoutProperties: RestTemplateTimeoutProperties
    ): RestClient {
        return RestClient.builder()
                .requestInterceptors { listOf(headerInterceptorRest) }
                .requestFactory(customRequestFactory(restTemplateTimeoutProperties))
                .build()
    }

    fun customRequestFactory(restTemplateTimeoutProperties: RestTemplateTimeoutProperties): ClientHttpRequestFactory {
        val settings: ClientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(restTemplateTimeoutProperties.connectionRestTemplateTimeoutInMillis)
                .withReadTimeout(restTemplateTimeoutProperties.readTimeoutRestTemplateInMillis)
        return ClientHttpRequestFactoryBuilder.detect().build(settings)
    }
}