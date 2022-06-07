package com.adadapted.library.network

import com.adadapted.library.event.EventClient
import com.adadapted.library.log.AALogger

object HttpErrorTracker {
    fun trackHttpError(errorCause: String, errorMessage: String, errorEventCode: String, url: String) {
        val params: MutableMap<String, String> = HashMap()
        params["url"] = url
        params["data"] = errorCause
        try {
            EventClient.trackSdkError(errorEventCode, errorMessage, params)
        } catch (illegalArg: IllegalArgumentException) {
            AALogger.logError("AppEventClient was not initialized, is your API key valid? DETAIL: " + illegalArg.message)
        }
    }
}