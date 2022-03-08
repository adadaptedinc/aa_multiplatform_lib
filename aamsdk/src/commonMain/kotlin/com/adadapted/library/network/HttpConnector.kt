package com.adadapted.library.network

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver

abstract class HttpConnector {
    val json = kotlinx.serialization.json.Json {
        useAlternativeNames = false
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(ResponseObserver) {
            onResponse { response ->
                println("HTTP status: ${response.status.value}")
            }
        }
    }
}