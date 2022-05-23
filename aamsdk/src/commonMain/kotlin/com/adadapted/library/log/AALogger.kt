package com.adadapted.library.log

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.adadapted.library.constants.Config

object AALogger {
    fun logInfo(message: String) {
        Logger.log(severity = Severity.Info, message = message, throwable = null)
    }

    fun logError(message: String) {
        Logger.log(severity = Severity.Error, message = message, throwable = null)
    }

    init {
        Logger.setTag(Config.LOG_TAG)
    }
}