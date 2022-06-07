package com.adadapted.library.device

import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.interfaces.DeviceCallback
import kotlin.native.concurrent.ThreadLocal

class DeviceInfoClient private constructor(
    private val appId: String,
    private val isProd: Boolean,
    private val params: Map<String, String>,
    private val customIdentifier: String,
    private val deviceInfoExtractor: DeviceInfoExtractor,
    private val transporter: TransporterCoroutineScope
) {

    private lateinit var deviceInfo: DeviceInfo
    private val deviceCallbacks: MutableSet<DeviceCallback>

    private fun performGetInfo(deviceCallback: DeviceCallback) {
        if (this::deviceInfo.isInitialized) {
            deviceCallback.onDeviceInfoCollected(deviceInfo)
        } else {
            deviceCallbacks.add(deviceCallback)
        }
    }

    private fun collectDeviceInfo() {
        this.deviceInfo = deviceInfoExtractor.extractDeviceInfo(appId, isProd, customIdentifier, params)
        notifyCallbacks()
    }

    private fun notifyCallbacks() {
        val currentDeviceCallbacks: Set<DeviceCallback> = HashSet(deviceCallbacks)
        for (caller in currentDeviceCallbacks) {
            caller.onDeviceInfoCollected(deviceInfo)
            deviceCallbacks.remove(caller)
        }
    }

    fun getDeviceInfo(deviceCallback: DeviceCallback) {
        transporter.dispatchToBackground {
            performGetInfo(deviceCallback)
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
        deviceCallbacks = HashSet()
        transporter.dispatchToBackground {
            collectDeviceInfo()
        }
    }
}