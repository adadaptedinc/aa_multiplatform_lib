package com.adadapted.library.payload

import com.adadapted.library.atl.AddItContent
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.device.DeviceInfoClient
import com.adadapted.library.interfaces.DeviceCallback
import com.adadapted.library.event.EventClient
import kotlin.jvm.Synchronized
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object PayloadClient {

    private var adapter: PayloadAdapter? = null
    private var eventClient: EventClient? = null
    private var transporter: TransporterCoroutineScope = Transporter()
    private const val PAYLOAD_ID = "payload_id"
    private const val TRACKING_ID = "tracking_id"
    private const val SOURCE = "source"
    private const val ITEM_NAME = "item_name"
    private var deeplinkInProgress = false

    private fun performPickupPayload(callback: (content: List<AddItContent>) -> Unit) {
        DeviceInfoClient.getDeviceInfo(object : DeviceCallback {
            override fun onDeviceInfoCollected(deviceInfo: DeviceInfo) {
                eventClient?.trackSdkEvent(EventStrings.PAYLOAD_PICKUP_ATTEMPT)
                transporter.dispatchToThread {
                    adapter?.pickup(deviceInfo) {
                        callback(it)
                    }
                }
            }
        })
    }

    private fun trackPayload(content: AddItContent, result: String) {
        val event = PayloadEvent(content.payloadId, result)
        transporter.dispatchToThread {
            DeviceInfoClient.getCachedDeviceInfo()
                ?.let { adapter?.publishEvent(it, event) }
        }
    }

    fun pickupPayloads(callback: (content: List<AddItContent>) -> Unit) {
        if (deeplinkInProgress) {
            return
        }
        transporter.dispatchToThread {
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
        transporter.dispatchToThread {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[SOURCE] = content.addItSource
            eventClient?.trackSdkEvent(EventStrings.ADDIT_ADDED_TO_LIST, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "delivered")
            }
        }
    }

    fun markContentItemAcknowledged(content: AddItContent, item: AddToListItem) {
        transporter.dispatchToThread {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[TRACKING_ID] = item.trackingId
            eventParams[ITEM_NAME] = item.title
            eventParams[SOURCE] = content.addItSource
            eventClient?.trackSdkEvent(EventStrings.ADDIT_ITEM_ADDED_TO_LIST, eventParams)
        }
    }

    fun markContentDuplicate(content: AddItContent) {
        transporter.dispatchToThread {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventClient?.trackSdkEvent(EventStrings.ADDIT_DUPLICATE_PAYLOAD, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "duplicate")
            }
        }
    }

    fun markContentFailed(content: AddItContent, message: String) {
        transporter.dispatchToThread {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventClient?.trackSdkError(EventStrings.ADDIT_CONTENT_FAILED, message, eventParams)
            if (content.isPayloadSource) {
                trackPayload(content, "rejected")
            }
        }
    }

    fun markContentItemFailed(content: AddItContent, item: AddToListItem, message: String) {
        transporter.dispatchToThread {
            val eventParams: MutableMap<String, String> = HashMap()
            eventParams[PAYLOAD_ID] = content.payloadId
            eventParams[TRACKING_ID] = item.trackingId
            eventClient?.trackSdkError(EventStrings.ADDIT_CONTENT_ITEM_FAILED, message, eventParams)
        }
    }

    fun createInstance(
        adapter: PayloadAdapter,
        eventClient: EventClient,
        transporter: TransporterCoroutineScope
    ) {
        this.adapter = adapter
        this.eventClient = eventClient
        this.transporter = transporter
    }
}
