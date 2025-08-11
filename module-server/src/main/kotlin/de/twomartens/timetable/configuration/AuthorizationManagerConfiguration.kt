package de.twomartens.timetable.configuration

import de.twomartens.timetable.configuration.roles.CheckSubject
import de.twomartens.timetable.configuration.roles.SubjectAuthorizationManager
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
        val pointcut = AnnotationMatchingPointcut(null, CheckSubject::class.java, true)
        val interceptor = AuthorizationManagerBeforeMethodInterceptor(pointcut, SubjectAuthorizationManager())
        interceptor.order = AuthorizationInterceptorsOrder.PRE_AUTHORIZE.order
        return interceptor
    }
}