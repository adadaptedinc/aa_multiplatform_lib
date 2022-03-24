package com.adadapted.library.network

import com.adadapted.library.keyword.InterceptAdapter
import com.adadapted.library.keyword.InterceptEvent
import com.adadapted.library.keyword.InterceptEventWrapper
import com.adadapted.library.session.Session
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.decodeFromJsonElement

class HttpInterceptAdapter(private val initUrl: String, private val eventUrl: String, private val httpConnector: HttpConnector) : InterceptAdapter {

    override suspend fun retrieve(session: Session, listener: InterceptAdapter.Listener) {
        if (session.id.isEmpty()) {
            return
        }

        try {
            val url =
                initUrl + "?aid=" + session.deviceInfo.appId + "&uid=" + session.deviceInfo.udid + "&sid=" + session.id + "&sdk=" + session.deviceInfo.sdkVersion
            val response: HttpResponse = httpConnector.client.get(url) {
                contentType(ContentType.Application.Json)
            }
            listener.onSuccess(httpConnector.json.decodeFromJsonElement(response.receive()))
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
        val compiledInterceptEventRequest = InterceptEventWrapper(
            session.id,
            session.deviceInfo.appId,
            session.deviceInfo.udid,
            session.deviceInfo.sdkVersion,
            events
        )

        try {
            httpConnector.client.post(eventUrl) {
                contentType(ContentType.Application.Json)
                body = compiledInterceptEventRequest
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