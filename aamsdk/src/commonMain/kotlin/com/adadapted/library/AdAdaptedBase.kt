package com.adadapted.library

abstract class AdAdaptedBase {
    enum class Env { PROD, DEV }

    var hasStarted = false
    var apiKey: String = ""
    var isProd = false
    val params: Map<String, String> = HashMap()
    lateinit var sessionListener: (hasAds: Boolean) -> Unit
    //private var eventListener: AaSdkEventListener? = null
    //private var contentListener: AaSdkAdditContentListener? = null

    fun withAppId(key: String): AdAdaptedBase {
        this.apiKey = key
        return this
    }

    fun inEnv(environment: Env): AdAdaptedBase {
        isProd = environment == Env.PROD
        return this
    }

    fun onHasAdsToServe(listener: (hasAds: Boolean) -> Unit): AdAdaptedBase {
        sessionListener = listener
        return this
    }

//    fun setSdkEventListener(listener: AaSdkEventListener): AdAdapted {
//        eventListener = listener
//        return this
//    }
//
//    fun setSdkAdditContentListener(listener: AaSdkAdditContentListener): AdAdapted {
//        contentListener = listener
//        return this
//    }
}