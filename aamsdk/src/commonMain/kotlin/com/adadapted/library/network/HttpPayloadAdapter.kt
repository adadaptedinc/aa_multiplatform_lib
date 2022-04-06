package com.adadapted.library.network

import com.adadapted.library.atl.AddItContent
import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.payload.PayloadAdapter
import com.adadapted.library.payload.PayloadEvent
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.decodeFromJsonElement

class HttpPayloadAdapter(private val pickupUrl: String, private val trackUrl: String, private val httpConnector: HttpConnector): PayloadAdapter {

    override suspend fun pickup(deviceInfo: DeviceInfo, callback: (content: List<AddItContent>) -> Unit) {
        try {
            val response: HttpResponse = httpConnector.client.post(pickupUrl) {
                contentType(ContentType.Application.Json)
                body = deviceInfo
            }

            val stringBody: String = response.receive()
            val check = stringBody

            callback(listOf())

//            listener.onSessionInitialized(
//                httpConnector.json.decodeFromJsonElement<Session>(response.receive())
//                    .apply { this.deviceInfo = deviceInfo })

        } catch (e: Exception) {
            println(e.message)
//            HttpErrorTracker.trackHttpError(
//                error,
//                pickupUrl,
//                EventStrings.PAYLOAD_PICKUP_REQUEST_FAILED,
//                LOGTAG
//            )
        }

    }

    override suspend fun publishEvent(event: PayloadEvent) {
//        val json = builder.buildEvent(event)
//        val request = JsonObjectRequest(
//            Request.Method.POST,
//            trackUrl,
//            json,
//            { },
//            { error ->
//                HttpErrorTracker.trackHttpError(
//                    error,
//                    trackUrl,
//                    EventStrings.PAYLOAD_EVENT_REQUEST_FAILED,
//                    LOGTAG
//                )
//            })
//        httpQueueManager.queueRequest(request)
    }
}