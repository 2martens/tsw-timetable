package de.twomartens.timetable.bahnApi.service

import mu.KotlinLogging
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler

class BahnResponseLogger : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return true
    }

    override fun handleError(response: ClientHttpResponse) {
        val body = response.body.bufferedReader().use { it.readText() }
        log.debug { "Status code [${response.statusCode.value()}], status text [${response.statusText}], body [$body]" }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}