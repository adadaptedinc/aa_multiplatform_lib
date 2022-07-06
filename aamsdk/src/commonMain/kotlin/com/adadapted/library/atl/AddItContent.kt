package com.adadapted.library.atl

import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient
import com.adadapted.library.payload.PayloadClient
import kotlin.jvm.Synchronized

data class AddItContent(
    val payloadId: String,
    val message: String,
    val image: String,
    val type: Int,
    private val source: String,
    val addItSource: String,
    private val items: List<AddToListItem>,
    private val payloadClient: PayloadClient = PayloadClient,
    private val eventClient: EventClient = EventClient
) : AddToListContent {

    internal object AddItSources {
        const val DEEPLINK = "deeplink"
        const val PAYLOAD = "payload"
    }

    private var handled: Boolean

    @Synchronized
    override fun acknowledge() {
        if (handled) {
            return
        }
        handled = true
        payloadClient.markContentAcknowledged(this)
    }

    @Synchronized
    override fun itemAcknowledge(item: AddToListItem) {
        if (!handled) {
            handled = true
            payloadClient.markContentAcknowledged(this)
        }
        payloadClient.markContentItemAcknowledged(this, item)
    }

    @Synchronized
    fun duplicate() {
        if (handled) {
            return
        }
        handled = true
        payloadClient.markContentDuplicate(this)
    }

    @Synchronized
    override fun failed(message: String) {
        if (handled) {
            return
        }
        handled = true
        payloadClient.markContentFailed(this, message)
    }

    @Synchronized
    override fun itemFailed(item: AddToListItem, message: String) {
        payloadClient.markContentItemFailed(this, item, message)
    }

    override fun getSource(): String {
        return source
    }

    val isPayloadSource: Boolean
        get() = addItSource == AddItSources.PAYLOAD

    override fun getItems(): List<AddToListItem> {
        return items
    }

    override fun hasNoItems(): Boolean {
        return items.isEmpty()
    }

    init {
        if (items.isEmpty()) {
            eventClient.trackSdkError(
                EventStrings.ADDIT_PAYLOAD_IS_EMPTY,
                ("Payload %s has empty payload$payloadId")
            )
        }
        handled = false
    }
}