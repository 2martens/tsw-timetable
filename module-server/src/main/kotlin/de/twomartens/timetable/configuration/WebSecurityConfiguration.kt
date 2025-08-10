package de.twomartens.timetable.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
open class WebSecurityConfiguration {
    @Bean
    @Throws(Exception::class)
    open fun securityFilterChain(http: HttpSecurity,
                                 @Qualifier("corsApplication") corsConfiguration: CorsConfigurationSource): SecurityFilterChain {
        http
                .cors { it.configurationSource(corsConfiguration) }
                .csrf { it.disable() }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .authorizeHttpRequests { it.requestMatchers(*PERMITTED_PATHS.toTypedArray<String>()).permitAll() }
                .authorizeHttpRequests { it.requestMatchers(HttpMethod.OPTIONS).permitAll() }
                .authorizeHttpRequests { it.anyRequest().authenticated() }
                .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
        return http.build()
    }

    companion object {
        private val PERMITTED_PATHS: Collection<String> = listOf(
                "/timetable/healthCheck",
                "/actuator/**",
                "/doc/v1/timetable/**",
                "/api-docs/v1/timetable/**",
                "/error",
                "/timetable/version",
        )
    }
}
