package de.twomartens.timetable.configuration

import de.twomartens.timetable.configuration.roles.CheckParty
import de.twomartens.timetable.configuration.roles.PartyAuthorizationManager
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut
import org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor

@Configuration
class AuthorizationManagerConfiguration {

    @Bean
    @Role(ROLE_INFRASTRUCTURE)
    fun checkPartyInterceptor(): AuthorizationManagerBeforeMethodInterceptor {
        val pointcut = AnnotationMatchingPointcut(null, CheckParty::class.java, true)
        val interceptor = AuthorizationManagerBeforeMethodInterceptor(pointcut, PartyAuthorizationManager())
        interceptor.order = AuthorizationInterceptorsOrder.PRE_AUTHORIZE.order
        return interceptor
    }
}