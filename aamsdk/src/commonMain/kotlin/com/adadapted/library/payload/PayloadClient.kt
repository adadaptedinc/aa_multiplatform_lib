package com.adadapted.library.payload

import com.adadapted.library.atl.AddItContent
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.interfaces.Callback
import kotlin.jvm.Synchronized
import kotlin.native.concurrent.ThreadLocal

class PayloadClient private constructor(
    private val adapter: PayloadAdapter,
    private val transporter: TransporterCoroutineScope
) { //private val appEventClient: AppEventClient,) {
    private fun performPickupPayload(callback: (content: List<AddItContent>) -> Unit) {
        DeviceInfoClient.getInstance().getDeviceInfo(object : Callback {
            override fun onDeviceInfoCollected(deviceInfo: DeviceInfo) {
                //appEventClient.trackSdkEvent(EventStrings.PAYLOAD_PICKUP_ATTEMPT)
                transporter.dispatchToBackground {
                    adapter.pickup(deviceInfo) {
                        callback(it)
                    }
                }
            }
        })
    }

    private fun trackPayload(content: AddItContent, result: String) {
        val event = PayloadEvent(content.payloadId, result)
        transporter.dispatchToBackground {
            DeviceInfoClient.getInstance().getCachedDeviceInfo()
                ?.let { adapter.publishEvent(it, event) }
        }
    }

    fun pickupPayloads(callback: (content: List<AddItContent>) -> Unit) {
        if (deeplinkInProgress) {
            return
        }
        transporter.dispatchToBackground {
            performPickupPayload(callback)
        }
    }

    @Synchronized
    fun deeplinkInProgress() {
        deeplinkInProgress = true
    }

    @Synchronized
    fun deeplinkCompleted() {
        deeplinkInProgress = false
    }

    fun markContentAcknowledged(content: AddItContent) {
        transporter.dispatchToBackground {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[SOURCE] = content.addItSource
            //appEventClient.trackSdkEvent(EventStrings.ADDIT_ADDED_TO_LIST, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "delivered")
            }
        }
    }

    fun markContentItemAcknowledged(content: AddItContent, item: AddToListItem) {
        transporter.dispatchToBackground {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[TRACKING_ID] = item.trackingId
            eventParams[ITEM_NAME] = item.title
            eventParams[SOURCE] = content.addItSource
            //appEventClient.trackSdkEvent(EventStrings.ADDIT_ITEM_ADDED_TO_LIST, eventParams)
        }
    }

    fun markContentDuplicate(content: AddItContent) {
        transporter.dispatchToBackground {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            //appEventClient.trackSdkEvent(EventStrings.ADDIT_DUPLICATE_PAYLOAD, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "duplicate")
            }
        }
    }

    fun markContentFailed(content: AddItContent, message: String) {
        transporter.dispatchToBackground {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            //appEventClient.trackError(EventStrings.ADDIT_CONTENT_FAILED, message, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "rejected")
            }
        }
    }

    fun markContentItemFailed(content: AddItContent, item: AddToListItem, message: String) {
        transporter.dispatchToBackground {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[TRACKING_ID] = item.trackingId
            //appEventClient.trackError(EventStrings.ADDIT_CONTENT_ITEM_FAILED, message, eventParams)
        }
    }

    @ThreadLocal
    companion object {
        private const val PAYLOAD_ID = "payload_id"
        private const val TRACKING_ID = "tracking_id"
        private const val SOURCE = "source"
        private const val ITEM_NAME = "item_name"
        private lateinit var instance: PayloadClient
        private var deeplinkInProgress = false

        fun getInstance(): PayloadClient {
            return instance
        }

        fun createInstance(
            adapter: PayloadAdapter,
            transporter: TransporterCoroutineScope
        ) { //appEventClient: AppEventClient,) {
            instance = PayloadClient(adapter, transporter) //appEventClient, transporter)
        }
    }
}