package com.adadapted.library.network

import com.adadapted.library.constants.Config
import com.adadapted.library.event.EventClient

object HttpErrorTracker {
    fun trackHttpError(errorCause: String, errorMessage: String, errorEventCode: String, url: String) {
        val params: MutableMap<String, String> = HashMap()
        params["url"] = url
        params["data"] = errorCause
        try {
            EventClient.getInstance().trackSdkError(errorEventCode, errorMessage, params)
        } catch (illegalArg: IllegalArgumentException) {
            print(Config.LOG_TAG + "AppEventClient was not initialized, is your API key valid? -DETAIL: " + illegalArg.message)
        }
    }
}