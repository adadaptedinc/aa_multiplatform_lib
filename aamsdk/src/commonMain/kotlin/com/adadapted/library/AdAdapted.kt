package com.adadapted.library

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config
import com.adadapted.library.network.HttpSessionAdapter
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AdAdapted {
    enum class Env { PROD, DEV }

    private var hasStarted = false
    private var apiKey: String = ""
    private var isProd = false
    private val params: Map<String, String> = HashMap()
    private lateinit var sessionListener: (hasAds: Boolean) -> Unit
    //private var eventListener: AaSdkEventListener? = null
    //private var contentListener: AaSdkAdditContentListener? = null
    private val LOG_TAG = AdAdapted::class.simpleName

    //private fun onHasAdsToServe(listener: (hasAds: Boolean) -> Unit) { }

    fun withAppId(key: String): AdAdapted {
        this.apiKey = key
        return this
    }

    fun inEnv(environment: Env): AdAdapted {
        isProd = environment == Env.PROD
        return this
    }

    fun setSdkSessionListener(listener: (hasAds: Boolean) -> Unit): AdAdapted {
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

    fun start() { //context
        if (apiKey.isEmpty()) {
            println(LOG_TAG + "The Api Key cannot be NULL")
            println("AdAdapted API Key Is Missing")
        }
        if (hasStarted) {
            if (!isProd) {
                println(LOG_TAG + "AdAdapted Android Advertising SDK has already been started")
            }
            return
        }
        hasStarted = true
        setupClients()
        //eventListener?.let { SdkEventPublisher.getInstance().setListener(it) }
        //contentListener?.let { AdditContentPublisher.getInstance().addListener(it) }
//        PayloadClient.getInstance().pickupPayloads(object : PayloadClient.Callback {
//            override fun onPayloadAvailable(content: List<AdditContent>) {
//                if (content.isNotEmpty()) {
//                    AdditContentPublisher.getInstance().publishAdditContent(content[0])
//                }
//            }
//        })

        val startListener: SessionListener = object : SessionListener {
            override fun onSessionAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
                if (session.hasActiveCampaigns() && !session.hasZoneAds()) {
                    println(LOG_TAG +"Session has ads to show but none were loaded properly. Is an obfuscation tool obstructing the AdAdapted Library?")
                }
            }

            override fun onAdsAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
            }

            override fun onSessionInitFailed() {
                sessionListener(false)
            }
        }
        SessionClient.getInstance().start(startListener)
        //AppEventClient.getInstance().trackSdkEvent(EventStrings.APP_OPENED)
        //KeywordInterceptMatcher.match("INIT") //init the matcher
        println(LOG_TAG + "AdAdapted Android Advertising SDK v%s initialized." + Config.VERSION_NAME)
    }

//    fun disableAdTracking(context: Context) {
//        setAdTracking(context, true)
//    }
//
//    fun enableAdTracking(context: Context) {
//        setAdTracking(context, false)
//    }
//
//    private fun setAdTracking(context: Context, value: Boolean) { //TODO needs to be android and ios specific - extract this out
//        val sharedPref = context.getSharedPreferences(Config.AASDK_PREFS_KEY, Context.MODE_PRIVATE) ?: return
//        with (sharedPref.edit()) {
//            putBoolean(Config.AASDK_PREFS_TRACKING_DISABLED_KEY, value)
//            apply()
//        }
//    }

    private fun setupClients() {
        Config.init(isProd)

        //DeviceInfoClient.createInstance(context.applicationContext, apiKey, isProd, params, DeviceInfoExtractor(), Transporter()) //TODO needs to be android and ios specific - extract this out
        SessionClient.createInstance(HttpSessionAdapter(Config.getInitSessionUrl(), Config.getRefreshAdsUrl()), Transporter())
        //AppEventClient.createInstance(HttpAppEventSink(Config.getAppEventsUrl(), Config.getAppErrorsUrl()), Transporter())
        //AdEventClient.createInstance(HttpAdEventSink(Config.getAdsEventUrl()), Transporter())
        //InterceptClient.createInstance(HttpInterceptAdapter(Config.getRetrieveInterceptsUrl(), Config.getInterceptEventsUrl()), Transporter())
        //PayloadClient.createInstance(HttpPayloadAdapter(Config.getPickupPayloadsUrl(), Config.getTrackingPayloadUrl()), AppEventClient.getInstance(), Transporter())
    }
}