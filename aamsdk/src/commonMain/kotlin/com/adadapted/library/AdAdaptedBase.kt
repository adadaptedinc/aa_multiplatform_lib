package com.adadapted.library

import com.adadapted.library.atl.AddToListContent

abstract class AdAdaptedBase {
    protected var hasStarted = false
    protected var apiKey: String = ""
    protected var isProd = false
    protected var customIdentifier: String = ""
    protected var isKeywordInterceptEnabled = false //disabled by default to save extra network calls
    protected val params: Map<String, String> = HashMap()
    protected lateinit var sessionListener: (hasAds: Boolean) -> Unit
    //private var eventListener: AaSdkEventListener? = null
    protected lateinit var contentListener: (atlContent: AddToListContent) -> Unit
}