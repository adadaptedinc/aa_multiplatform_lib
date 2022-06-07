package com.adadapted.library.network

import com.adadapted.library.constants.EventStrings
import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.log.AALogger
import com.adadapted.library.interfaces.AdGetListener
import com.adadapted.library.interfaces.SessionInitListener
import com.adadapted.library.session.Session
import com.adadapted.library.interfaces.SessionAdapter
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HttpSessionAdapter(
    private val initUrl: String,
    private val refreshUrl: String,
    private val httpConnector: HttpConnector
) : SessionAdapter {
    override suspend fun sendInit(
        deviceInfo: DeviceInfo,
        listener: SessionInitListener
    ) {
        try {
            val response: HttpResponse = httpConnector.client.post(initUrl) {
                contentType(ContentType.Application.Json)
                setBody(deviceInfo)
            }
            listener.onSessionInitialized(response.body<Session>().apply { this.deviceInfo = deviceInfo })

        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.SESSION_REQUEST_FAILED,
                initUrl
            )
            listener.onSessionInitializeFailed()
        }
    }

    override suspend fun sendRefreshAds(session: Session, listener: AdGetListener) {
        try {
            val url = refreshUrl + ("?aid=" + session.deviceInfo.appId + "&uid=" + session.deviceInfo.udid + "&sid=" + session.id + "&sdk=" + session.deviceInfo.sdkVersion)
            val response: HttpResponse = httpConnector.client.get(url) {
                contentType(ContentType.Application.Json)
            }
            listener.onNewAdsLoaded(response.body())
        } catch (e: Exception) {
            e.message?.let { AALogger.logError(it) }
            HttpErrorTracker.trackHttpError(
                e.cause.toString(),
                e.message.toString(),
                EventStrings.AD_GET_REQUEST_FAILED,
                refreshUrl
            )
            listener.onNewAdsLoadFailed()
        }
    }
}
