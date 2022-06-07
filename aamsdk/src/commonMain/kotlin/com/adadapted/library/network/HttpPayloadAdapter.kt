package com.adadapted.library.network

import com.adadapted.library.atl.AddItContent
import com.adadapted.library.atl.AddItContentParser
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.log.AALogger
import com.adadapted.library.payload.PayloadAdapter
import com.adadapted.library.payload.PayloadEvent
import com.adadapted.library.payload.PayloadRequestBuilder
import com.adadapted.library.payload.PayloadResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class HttpPayloadAdapter(
    private val pickupUrl: String,
    private val trackUrl: String,
    private val httpConnector: HttpConnector
) : PayloadAdapter {
    override suspend fun pickup(deviceInfo: DeviceInfo, callback: (content: List<AddItContent>) -> Unit) {
        try {
            val response: HttpResponse = httpConnector.client.post(pickupUrl) {
                contentType(ContentType.Application.Json)
                setBody(PayloadRequestBuilder.buildRequest(deviceInfo))
            }
            response.body<PayloadResponse>().apply { AddItContentParser.generateAddItContentFromPayloads(this).apply(callback) }
        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.PAYLOAD_PICKUP_REQUEST_FAILED,
                pickupUrl
            )
        }
    }

    override suspend fun publishEvent(deviceInfo: DeviceInfo, event: PayloadEvent) {
        try {
            httpConnector.client.post(trackUrl) {
                contentType(ContentType.Application.Json)
                setBody(PayloadRequestBuilder.buildEventRequest(deviceInfo, event))
            }
        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.PAYLOAD_EVENT_REQUEST_FAILED,
                trackUrl
            )
        }
    }
}