package com.adadapted.library.network

import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.concurrency.Transporter
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver
import kotlin.native.concurrent.ThreadLocal

class HttpConnector {
    val json = kotlinx.serialization.json.Json {
        useAlternativeNames = false
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    val client = HttpClient {
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
            if (!this::instance.isInitialized) {
                instance = HttpConnector()
            }
            return instance
        }
    }
}