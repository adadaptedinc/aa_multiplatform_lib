package com.adadapted.library

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.device.DeviceInfoExtractor
import com.adadapted.library.network.HttpSessionAdapter
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener

object IosAdAdapted: AdAdaptedBase() {

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
                    println(LOG_TAG +"Session has ads to show but none were loaded properly. Is an obfuscation tool obstructing the AdAdapted Library?")
                }
            }

            override fun onAdsAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
                println("onAdsAvailable")
            }

            override fun onSessionInitFailed() {
                sessionListener(false)
                println("onSessionInitFailed")
            }
        }
        SessionClient.getInstance()?.start(startListener)
        println(LOG_TAG + "AdAdapted iOS Advertising SDK v%s initialized." + Config.VERSION_NAME)
    }

    private fun setupClients() {
        Config.init(isProd)

        val deviceInfoExtractor = DeviceInfoExtractor()
        DeviceInfoClient.createInstance(apiKey, isProd, params, customIdentifier, deviceInfoExtractor, Transporter())
        SessionClient.createInstance(HttpSessionAdapter(Config.getInitSessionUrl(), Config.getRefreshAdsUrl()), Transporter())
    }
}