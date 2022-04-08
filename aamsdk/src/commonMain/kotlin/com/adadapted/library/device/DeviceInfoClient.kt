package com.adadapted.library.device

import com.adadapted.library.concurrency.TransporterCoroutineScope
import kotlin.native.concurrent.ThreadLocal

class DeviceInfoClient private constructor(
    private val appId: String,
    private val isProd: Boolean,
    private val params: Map<String, String>,
    private val customIdentifier: String,
    private val deviceInfoExtractor: DeviceInfoExtractor,
    private val transporter: TransporterCoroutineScope
) {
    interface Callback {
        fun onDeviceInfoCollected(deviceInfo: DeviceInfo)
    }

    private lateinit var deviceInfo: DeviceInfo
    private val callbacks: MutableSet<Callback>

    private fun performGetInfo(callback: Callback) {
        if (this::deviceInfo.isInitialized) {
            callback.onDeviceInfoCollected(deviceInfo)
        } else {
            callbacks.add(callback)
        }
    }

    private fun collectDeviceInfo() {
        this.deviceInfo = deviceInfoExtractor.extractDeviceInfo(appId, isProd, customIdentifier, params)
        notifyCallbacks()
    }

    private fun notifyCallbacks() {
        val currentCallbacks: Set<Callback> = HashSet(callbacks)
        for (caller in currentCallbacks) {
            caller.onDeviceInfoCollected(deviceInfo)
            callbacks.remove(caller)
        }
    }

    fun getDeviceInfo(callback: Callback) {
        transporter.dispatchToBackground {
            performGetInfo(callback)
        }
    }

    fun getCachedDeviceInfo(): DeviceInfo? {
        return if (this::deviceInfo.isInitialized) {
            deviceInfo
        } else null
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: DeviceInfoClient

        fun createInstance(
            appId: String,
            isProd: Boolean,
            params: Map<String, String>,
            customIdentifier: String,
            deviceInfoExtractor: DeviceInfoExtractor,
            transporter: TransporterCoroutineScope
        ) {
            instance = DeviceInfoClient(
                appId,
                isProd,
                params,
                customIdentifier,
                deviceInfoExtractor,
                transporter
            )
        }

        fun getInstance(): DeviceInfoClient {
            return instance
        }
    }

    init {
        callbacks = HashSet()
        transporter.dispatchToBackground {
            collectDeviceInfo()
        }
    }
}