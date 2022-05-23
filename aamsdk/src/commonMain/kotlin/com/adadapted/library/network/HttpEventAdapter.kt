package com.adadapted.library.network

import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.*
import com.adadapted.library.log.AALogger
import com.adadapted.library.session.Session
import io.ktor.client.request.*
import io.ktor.http.*

class HttpEventAdapter(private val adEventUrl: String, private val sdkEventUrl: String, private val errorUrl: String, private val httpConnector: HttpConnector) :
    EventAdapter {
    override suspend fun publishAdEvents(session: Session, adEvents: Set<AdEvent>) {
        try {
            httpConnector.client.post(adEventUrl) {
                contentType(ContentType.Application.Json)
                setBody(EventRequestBuilder.buildAdEventRequest(session, adEvents))
            }
        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.AD_EVENT_TRACK_REQUEST_FAILED,
                adEventUrl
            )
        }
    }

    override suspend fun publishSdkEvents(session: Session, events: Set<SdkEvent>) {
        try {
            httpConnector.client.post(sdkEventUrl) {
                contentType(ContentType.Application.Json)
                setBody(EventRequestBuilder.buildEventRequest(session, sdkEvents = events))
            }
        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.SDK_EVENT_REQUEST_FAILED,
                adEventUrl
            )
        }
    }

    override suspend fun publishSdkErrors(session: Session, errors: Set<SdkError>) {
        try {
            httpConnector.client.post(errorUrl) {
                contentType(ContentType.Application.Json)
                setBody(EventRequestBuilder.buildEventRequest(session, sdkErrors = errors))
            }
        } catch (e: Exception) {
            AALogger.logError("SDK Error Request Failed -> " + e.message)
        }
    }
}