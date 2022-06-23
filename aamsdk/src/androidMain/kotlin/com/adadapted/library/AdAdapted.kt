package com.adadapted.library

import android.content.Context
import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.atl.AddToListContent
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.device.DeviceInfoExtractor
import com.adadapted.library.event.EventClient
import com.adadapted.library.event.EventBroadcaster
import com.adadapted.library.interfaces.AddItContentListener
import com.adadapted.library.interfaces.EventBroadcastListener
import com.adadapted.library.keyword.InterceptClient
import com.adadapted.library.keyword.InterceptMatcher
import com.adadapted.library.log.AALogger
import com.adadapted.library.network.*
import com.adadapted.library.payload.PayloadClient
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener

object AdAdapted : AdAdaptedBase() {

    fun withAppId(key: String): AdAdapted {
        this.apiKey = key
        return this
    }

    fun inEnvironment(env: AdAdaptedEnv): AdAdapted {
        isProd = env == AdAdaptedEnv.PROD
        return this
    }

    fun onHasAdsToServe(listener: (hasAds: Boolean) -> Unit): AdAdapted {
        sessionListener = listener
        return this
    }

    fun enableKeywordIntercept(value: Boolean): AdAdapted {
        isKeywordInterceptEnabled = value
        return this
    }

    fun setSdkEventListener(listener: EventBroadcastListener): AdAdapted {
        eventListener = listener
        return this
    }

    fun setSdkAddItContentListener(listener: AddItContentListener): AdAdapted {
        contentListener = listener
        return this
    }

    fun start(context: Context) {
        if (apiKey.isEmpty()) {
            AALogger.logError("The AdAdapted Api Key is missing or NULL")
        }
        if (hasStarted) {
            if (!isProd) {
                AALogger.logError("AdAdapted Android Advertising SDK has already been started.")
            }
        }
        hasStarted = true
        setupClients(context)
        eventListener.let { EventBroadcaster.setListener(it) }
        contentListener.let { AddItContentPublisher.addListener(it) }
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
                    AALogger.logError("The session has ads to show but none were loaded properly. Is an obfuscation tool obstructing the AdAdapted Library?")
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
        AALogger.logInfo("AdAdapted Android Multiplatform SDK ${Config.VERSION_NAME} initialized.")
    }

    fun setCustomIdentifier(identifier: String): AdAdaptedBase {
        customIdentifier = identifier
        return this
    }

    fun disableAdTracking(context: Context): AdAdaptedBase {
        setAdTracking(context, true)
        return this
    }

    fun enableAdTracking(context: Context): AdAdaptedBase {
        setAdTracking(context, false)
        return this
    }

    private fun setAdTracking(context: Context, value: Boolean) {
        val sharedPref =
            context.getSharedPreferences(Config.AASDK_PREFS_KEY, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(Config.AASDK_PREFS_TRACKING_DISABLED_KEY, value)
            apply()
        }
    }

    private fun setupClients(context: Context) {
        Config.init(isProd)

        val deviceInfoExtractor = DeviceInfoExtractor(context)
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

