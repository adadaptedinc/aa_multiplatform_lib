package com.adadapted.library.network

import com.adadapted.library.keyword.InterceptAdapter
import com.adadapted.library.keyword.InterceptEvent
import com.adadapted.library.session.Session
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.decodeFromJsonElement

class HttpInterceptAdapter(private val initUrl: String, private val eventUrl: String) :
    HttpConnector(), InterceptAdapter {

    override suspend fun retrieve(session: Session, listener: InterceptAdapter.Listener) {
        if (session.id.isEmpty()) {
            return
        }

        try {
            httpClient.use {
                val url =
                    initUrl + "?aid=" + session.deviceInfo.appId + "&uid=" + session.deviceInfo.udid + "&sid=" + session.id + "&sdk=" + session.deviceInfo.sdkVersion
                val response: HttpResponse = httpClient.get(url) {
                    contentType(ContentType.Application.Json)
                }
                listener.onSuccess(json.decodeFromJsonElement(response.receive()))
            }
        } catch (e: Exception) {
            println(e.message)
//          HttpErrorTracker.trackHttpError(
//                    error,
//                    initUrl,
//                    EventStrings.KI_SESSION_REQUEST_FAILED,
//                    LOGTAG
//                )
        }
    }


    override suspend fun sendEvents(session: Session, events: MutableSet<InterceptEvent>) {
        try {
            httpClient.use {
                httpClient.post(eventUrl) {
                    contentType(ContentType.Application.Json)
                    body = events
                }
            }
        } catch (e: Exception) {
            println(e.message)
//            HttpErrorTracker.trackHttpError(
//                error,
//                eventUrl,
//                EventStrings.KI_EVENT_REQUEST_FAILED,
//                LOGTAG
//            )
        }
    }
}