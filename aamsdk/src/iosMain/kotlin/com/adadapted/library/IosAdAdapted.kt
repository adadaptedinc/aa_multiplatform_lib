package com.adadapted.library

import com.adadapted.library.atl.AddToListContent
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.device.DeviceInfoExtractor
import com.adadapted.library.keyword.InterceptClient
import com.adadapted.library.keyword.InterceptMatcher
import com.adadapted.library.network.HttpConnector
import com.adadapted.library.network.HttpInterceptAdapter
import com.adadapted.library.network.HttpPayloadAdapter
import com.adadapted.library.network.HttpSessionAdapter
import com.adadapted.library.payload.PayloadClient
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import platform.Foundation.valueForKey

object IosAdAdapted : AdAdaptedBase() {

    fun withAppId(key: String): IosAdAdapted {
        this.apiKey = key
        return this
    }

    fun inEnvironment(env: AdAdaptedEnv): IosAdAdapted {
        isProd = env == AdAdaptedEnv.PROD
        return this
    }

    fun onHasAdsToServe(listener: (hasAds: Boolean) -> Unit): IosAdAdapted {
        sessionListener = listener
        return this
    }

    fun enableKeywordIntercept(value: Boolean): IosAdAdapted {
        isKeywordInterceptEnabled = value
        return this
    }

    fun setSdkAddItContentListener(listener: (atlContent: AddToListContent) -> Unit): IosAdAdapted {
        contentListener = listener
        return this
    }

    @Throws(Exception::class)
    fun start() {
        if (apiKey.isEmpty()) {
            println(LOG_TAG + "The Api Key cannot be NULL")
            println("AdAdapted API Key Is Missing")
        }
        if (hasStarted) {
            if (!isProd) {
                println(LOG_TAG + "AdAdapted iOS Advertising SDK has already been started")
            }
        }
        hasStarted = true
        setupClients()

        val startListener: SessionListener = object : SessionListener {
            override fun onSessionAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
                if (session.hasActiveCampaigns() && !session.hasZoneAds()) {
                    println(LOG_TAG + "Session has ads to show but none were loaded properly. Is an obfuscation tool obstructing the AdAdapted Library?")
                }
            }

            override fun onAdsAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
            }

            override fun onSessionInitFailed() {
                sessionListener(false)
            }
        }
        SessionClient.start(startListener)

        if (isKeywordInterceptEnabled) {
            InterceptMatcher.match("INIT") //init the matcher
        }
        println(LOG_TAG + "AdAdapted iOS Advertising SDK v%s initialized." + Config.VERSION_NAME)
    }

    fun setCustomIdentifier(identifier: String): AdAdaptedBase {
        customIdentifier = identifier
        return this
    }

    fun disableAdTracking(): AdAdaptedBase {
        setAdTracking(true)
        return this
    }

    fun enableAdTracking(): AdAdaptedBase {
        setAdTracking(false)
        return this
    }

    private fun setAdTracking(value: Boolean) {
        val preferences = NSUserDefaults.standardUserDefaults
        preferences.setValue(value = value, forKey = Config.AASDK_PREFS_TRACKING_DISABLED_KEY)
    }

    private fun iosEndpoint(string: String) : String {
        return string.replace("android","ios", true)
    }

    private fun setupClients() {
        Config.init(isProd)

        val deviceInfoExtractor = DeviceInfoExtractor()

        DeviceInfoClient.createInstance(
            apiKey,
            isProd,
            params,
            customIdentifier,
            deviceInfoExtractor,
            Transporter()
        )
        SessionClient.createInstance(
            HttpSessionAdapter(
                Config.getInitSessionUrl(),
                Config.getRefreshAdsUrl(),
                HttpConnector.getInstance()
            ), Transporter()
        )
        InterceptClient.createInstance(
            HttpInterceptAdapter(
                Config.getRetrieveInterceptsUrl(),
                Config.getInterceptEventsUrl(),
                HttpConnector.getInstance()
            ), Transporter()
        )
        PayloadClient.createInstance(
            HttpPayloadAdapter(
                Config.getPickupPayloadsUrl(),
                Config.getTrackingPayloadUrl(),
                HttpConnector.getInstance()
            ), Transporter()
        )
    }
}