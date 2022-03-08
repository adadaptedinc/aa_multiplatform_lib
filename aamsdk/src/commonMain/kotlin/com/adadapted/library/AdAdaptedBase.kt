package com.adadapted.library

abstract class AdAdaptedBase {
    var hasStarted = false
    var apiKey: String = ""
    var isProd = false
    val params: Map<String, String> = HashMap()
    lateinit var sessionListener: (hasAds: Boolean) -> Unit
    //private var eventListener: AaSdkEventListener? = null
    //private var contentListener: AaSdkAdditContentListener? = null
}