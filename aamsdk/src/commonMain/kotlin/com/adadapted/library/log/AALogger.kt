package com.adadapted.library.log

import com.adadapted.library.constants.Config
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object AALogger {
    private var isDebugLoggingEnabled = false

    fun logDebug(message: String) {
        if (isDebugLoggingEnabled) {
            Napier.d(message = message, tag = Config.LOG_TAG)
        }
    }

    fun logInfo(message: String) {
        Napier.i(message = message, tag = Config.LOG_TAG)
    }

    fun logError(message: String) {
        Napier.e(message = message, tag = Config.LOG_TAG)
    }

    fun enableDebugLogging() {
        isDebugLoggingEnabled = true
    }

    init {
        Napier.base(DebugAntilog())
    }
}