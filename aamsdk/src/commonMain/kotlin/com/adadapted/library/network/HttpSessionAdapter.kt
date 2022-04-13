package com.adadapted.library.network

import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.interfaces.AdGetListener
import com.adadapted.library.interfaces.SessionInitListener
import com.adadapted.library.session.Session
import com.adadapted.library.interfaces.SessionAdapter
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.decodeFromJsonElement

class HttpSessionAdapter(private val initUrl: String, private val refreshUrl: String, private val httpConnector: HttpConnector) :
    SessionAdapter {

    override suspend fun sendInit(
        deviceInfo: DeviceInfo,
        listener: SessionInitListener
    ) {
        try {
            val response: HttpResponse = httpConnector.client.post(initUrl) {
                contentType(ContentType.Application.Json)
                body = deviceInfo
            }
            listener.onSessionInitialized(
                httpConnector.json.decodeFromJsonElement<Session>(response.receive())
                    .apply { this.deviceInfo = deviceInfo })

        } catch (e: Exception) {
            println(e.message)
//            HttpErrorTracker.trackHttpError(
//                    error,
//                    initUrl,
//                    EventStrings.SESSION_REQUEST_FAILED,
//                    LOGTAG
//                )
            listener.onSessionInitializeFailed()
        }
    }

    override suspend fun sendRefreshAds(session: Session, listener: AdGetListener) {
        try {
            val url =
                refreshUrl + ("?aid=" + session.deviceInfo.appId + "&uid=" + session.deviceInfo.udid + "&sid=" + session.id + "&sdk=" + session.deviceInfo.sdkVersion)

            val response: HttpResponse = httpConnector.client.get(url) {
                contentType(ContentType.Application.Json)
            }
            listener.onNewAdsLoaded(
                httpConnector.json.decodeFromJsonElement(response.receive())
            )
        } catch (e: Exception) {
            println(e.message)
//            HttpErrorTracker.trackHttpError(
//                    error,
//                    refreshUrl,
//                    EventStrings.AD_GET_REQUEST_FAILED,
//                    LOGTAG
//                )
            listener.onNewAdsLoadFailed()
        }
    }
}
