package com.adadapted.library.atl

import com.adadapted.library.atl.AddToListContent.Sources.IN_APP
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
    private var payloadClient: PayloadClient = PayloadClient.getInstance()
//appEventClient: AppEventClient = AppEventClient.getInstance(),
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

    companion object {
        //TODO change these to map functions
        fun createDeeplinkContent(
            payloadId: String,
            message: String,
            image: String,
            type: Int,
            items: List<AddToListItem>
        ): AddItContent {
            return AddItContent(
                payloadId,
                message,
                image,
                type,
                AddToListContent.Sources.OUT_OF_APP,
                AddItSources.DEEPLINK,
                items
            )
        }

        fun createInAppContent(
            payloadId: String,
            message: String,
            image: String,
            type: Int,
            items: List<AddToListItem>
        ): AddItContent {
            return AddItContent(
                payloadId,
                message,
                image,
                type,
                IN_APP,
                IN_APP,
                items
            )
        }
    }

    init {
        if (items.isEmpty()) {
            //appEventClient.trackError(EventStrings.ADDIT_PAYLOAD_IS_EMPTY, ("Payload %s has empty payload$payloadId"))
        }
        handled = false
    }
}