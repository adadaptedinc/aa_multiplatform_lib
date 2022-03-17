package com.adadapted.library.network

import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionAdapter
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.core.use
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer

class HttpSessionAdapter(private val initUrl: String, private val refreshUrl: String) :
    HttpConnector(), SessionAdapter {

    override suspend fun sendInit(
        deviceInfo: DeviceInfo,
        listener: SessionAdapter.SessionInitListener
    ) {
        try {
            httpClient.use {
                val response: HttpResponse = httpClient.post(initUrl) {
                    contentType(ContentType.Application.Json)
                    body = deviceInfo
                }
                listener.onSessionInitialized(
                    json.decodeFromJsonElement<Session>(response.receive())
                        .apply { this.deviceInfo = deviceInfo })
            }
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

    override suspend fun sendRefreshAds(session: Session, listener: SessionAdapter.AdGetListener) {
        try {
            httpClient.use {
                val url =
                    refreshUrl + ("?aid=%s" + session.deviceInfo.appId + "&uid=%s" + session.deviceInfo.udid + "&sid=%s" + session.id + "&sdk=%s" + session.deviceInfo.sdkVersion)

                val response: HttpResponse = httpClient.get(url) {
                    contentType(ContentType.Application.Json)
                }
                listener.onNewAdsLoaded(
                    json.decodeFromJsonElement(response.receive())
                )
            }
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