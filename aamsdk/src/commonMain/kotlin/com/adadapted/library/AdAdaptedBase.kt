package com.adadapted.library

import com.adadapted.library.atl.AddToListContent

abstract class AdAdaptedBase {
    var hasStarted = false
    var apiKey: String = ""
    var isProd = false
    val params: Map<String, String> = HashMap()
    lateinit var sessionListener: (hasAds: Boolean) -> Unit
    //private var eventListener: AaSdkEventListener? = null
    lateinit var contentListener: (atlContent: AddToListContent) -> Unit
}