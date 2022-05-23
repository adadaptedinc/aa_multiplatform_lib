package com.adadapted.library.network

import com.adadapted.library.log.AALogger
import io.ktor.client.engine.*
import io.ktor.client.HttpClient
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.observer.*
import kotlin.native.concurrent.ThreadLocal
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect val defaultPlatformEngine: HttpClientEngine
class HttpConnector private constructor() {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                useAlternativeNames = false
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }

        install(ResponseObserver) {
            onResponse { response ->
                AALogger.logInfo("HTTP status: ${response.status.value}")
            }
        }

        install(HttpRequestRetry)
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: HttpConnector

        fun getInstance(): HttpConnector {
            if (!this::instance.isInitialized) {
                instance = HttpConnector()
            }
            return instance
        }
    }
}
