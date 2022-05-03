package com.adadapted.library.network

import com.adadapted.library.event.AdEvent
import com.adadapted.library.event.EventAdapter
import com.adadapted.library.event.EventRequestBuilder
import com.adadapted.library.session.Session
import io.ktor.client.request.*
import io.ktor.http.*

class HttpEventAdapter(private val adEventUrl: String, private val httpConnector: HttpConnector) :
    EventAdapter {

    override suspend fun sendAdEvents(session: Session, adEvents: Set<AdEvent>) {
        try {
            httpConnector.client.post(adEventUrl) {
                contentType(ContentType.Application.Json)
                body = EventRequestBuilder.buildAdEventRequest(session, adEvents)
            }
        } catch (e: Exception) {
            println(e.message)
//            HttpErrorTracker.trackHttpError(
//                error,
//                trackUrl,
//                EventStrings.PAYLOAD_EVENT_REQUEST_FAILED,
//                LOGTAG
//            )
        }
    }
}