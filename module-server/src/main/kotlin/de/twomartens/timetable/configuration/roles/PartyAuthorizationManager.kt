package de.twomartens.timetable.configuration.roles

import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.authorization.AuthorizationResult
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler
import org.springframework.security.core.Authentication
import java.util.function.Supplier

class PartyAuthorizationManager : AuthorizationManager<MethodInvocation>, MethodAuthorizationDeniedHandler {
    @Deprecated("Deprecated in Spring Security")
    override fun check(authentication: Supplier<Authentication?>?, invocation: MethodInvocation?): AuthorizationDecision {
        if (authentication == null || authentication.get() == null || !authentication.get()!!.isAuthenticated) {
            return AuthorizationDecision(false)
        }

        val annotation = invocation!!.method.getAnnotation(CheckParty::class.java)
                ?: invocation.method.declaringClass.getAnnotation(CheckParty::class.java)
                ?: return AuthorizationDecision(true) // No annotation, allow

        val authentication = authentication.get()!!
        val partyParamName = annotation.partyParamName

        // 1. Authorized party check
        val methodParams = invocation.method.parameters
        val argIndex = methodParams.indexOfFirst { it.name == partyParamName }
        if (argIndex == -1) {
            return AuthorizationDecision(false)
        }

        val requestedPartyId = invocation.arguments[argIndex]?.toString()
        val tokenPartyId = (authentication.details as? Map<*, *>)?.get("azp")?.toString()
                ?: authentication.name // fallback

        return AuthorizationDecision(requestedPartyId == tokenPartyId)
    }

    override fun handleDeniedInvocation(methodInvocation: MethodInvocation?, authorizationResult: AuthorizationResult?): Any? {
        throw AuthorizationDeniedException("The requested resources are not accessible to the authorized party")
    }
}