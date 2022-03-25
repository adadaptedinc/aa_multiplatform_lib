package com.adadapted.library.network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver
import kotlinx.serialization.serializer
import kotlin.native.concurrent.ThreadLocal

expect val defaultPlatformEngine: HttpClientEngine

class HttpConnector {
    val json = kotlinx.serialization.json.Json {
        useAlternativeNames = false
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    val client = HttpClient(defaultPlatformEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(ResponseObserver) {
            onResponse { response ->
                println("HTTP status: ${response.status.value}")
            }
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: HttpConnector

        fun getInstance(): HttpConnector {
            return if (::instance.isInitialized) {
                instance
            } else {
                instance = HttpConnector()
                instance
            }
        }
    }
}