package com.adadapted.library

import com.adadapted.library.interfaces.AddItContentListener
import com.adadapted.library.interfaces.EventBroadcastListener
import com.adadapted.library.interfaces.SessionBroadcastListener

abstract class AdAdaptedBase {
    protected var hasStarted = false
    protected var apiKey: String = ""
    protected var isProd = false
    protected var customIdentifier: String = ""
    protected var isKeywordInterceptEnabled = false //disabled by default to save extra network calls
    protected val params: Map<String, String> = HashMap()
    protected lateinit var sessionListener: SessionBroadcastListener
    protected lateinit var eventListener: EventBroadcastListener
    protected lateinit var contentListener: AddItContentListener
}