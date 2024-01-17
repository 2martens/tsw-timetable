package de.twomartens.timetable.bahnApi.service

import mu.KotlinLogging
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler

class BahnErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode.isError
    }

    override fun handleError(response: ClientHttpResponse) {
        log.debug { "Error response: $response" }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}