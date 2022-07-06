package com.adadapted.library

import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.device.DeviceInfoExtractor
import com.adadapted.library.event.EventBroadcaster
import com.adadapted.library.event.EventClient
import com.adadapted.library.interfaces.AddItContentListener
import com.adadapted.library.interfaces.EventBroadcastListener
import com.adadapted.library.interfaces.EventClientListener
import com.adadapted.library.keyword.InterceptClient
import com.adadapted.library.keyword.InterceptMatcher
import com.adadapted.library.log.AALogger
import com.adadapted.library.network.*
import com.adadapted.library.payload.PayloadClient
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

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

    fun setSdkEventListener(listener: EventBroadcastListener): IosAdAdapted {
        eventListener = listener
        return this
    }

    fun setSdkAddItContentListener(listener: AddItContentListener): IosAdAdapted {
        contentListener = listener
        return this
    }

    fun enableDebugLogging(): IosAdAdapted {
        AALogger.enableDebugLogging()
        return this
    }

    fun setCustomIdentifier(identifier: String): IosAdAdapted {
        customIdentifier = identifier
        return this
    }

    fun disableAdTracking(): IosAdAdapted {
        setAdTracking(true)
        return this
    }

    fun enableAdTracking(): IosAdAdapted {
        setAdTracking(false)
        return this
    }

    @Throws(Exception::class)
    fun start() {
        if (apiKey.isEmpty()) {
            AALogger.logError("The AdAdapted Api Key is missing or NULL")
        }
        if (hasStarted) {
            if (!isProd) {
                AALogger.logError("AdAdapted iOS Advertising SDK has already been started")
            }
        }
        hasStarted = true
        setupClients()

        eventListener.let {
            EventBroadcaster.setListener(it)
        }
        contentListener.let {
            AddItContentPublisher.addListener(it)
        }

        PayloadClient.pickupPayloads {
            if (it.isNotEmpty()) {
                for (content in it) {
                    AddItContentPublisher.publishAddItContent(content)
                }
            }
        }

        val startListener: SessionListener = object : SessionListener {
            override fun onSessionAvailable(session: Session) {
                sessionListener(session.hasActiveCampaigns())
                if (session.hasActiveCampaigns() && !session.hasZoneAds()) {
                    AALogger.logError("Session has ads to show but none were loaded properly. Is an obfuscation tool obstructing the AdAdapted Library?")
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
        EventClient.trackSdkEvent(EventStrings.APP_OPENED)

        if (isKeywordInterceptEnabled) {
            InterceptMatcher.match("INIT") //init the matcher
        }
        AALogger.logInfo("AdAdapted iOS Advertising SDK v%s initialized." + Config.VERSION_NAME)
    }

    private fun setAdTracking(value: Boolean) {
        val preferences = NSUserDefaults.standardUserDefaults
        preferences.setValue(value = value, forKey = Config.AASDK_PREFS_TRACKING_DISABLED_KEY)
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
                HttpConnector
            ), Transporter()
        )
        EventClient.createInstance(
            HttpEventAdapter(
                Config.getAdEventsUrl(),
                Config.getSdkEventsUrl(),
                Config.getSdkErrorsUrl(),
                HttpConnector
            ), Transporter()
        )
        InterceptClient.createInstance(
            HttpInterceptAdapter(
                Config.getRetrieveInterceptsUrl(),
                Config.getInterceptEventsUrl(),
                HttpConnector
            ), Transporter()
        )
        PayloadClient.createInstance(
            HttpPayloadAdapter(
                Config.getPickupPayloadsUrl(),
                Config.getTrackingPayloadUrl(),
                HttpConnector
            ), EventClient, Transporter()
        )
    }
}