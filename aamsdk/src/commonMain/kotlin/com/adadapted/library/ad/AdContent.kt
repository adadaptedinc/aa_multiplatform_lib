package com.adadapted.library.ad

import com.adadapted.library.atl.AddToListContent
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient
import kotlin.jvm.Synchronized

//class AdContent private constructor(private val ad: Ad, val type: Int, private val items: List<AddToListItem>, adClient: AdEventClient = AdEventClient.getInstance(), appClient: AppEventClient = AppEventClient.getInstance()) : AddToListContent {
class AdContent private constructor(private val ad: Ad, val type: Int, private val items: List<AddToListItem>, private val eventClient: EventClient = EventClient.getInstance()) : AddToListContent {
    private var isHandled: Boolean
    //private var appEventClient: AppEventClient = appClient

    override fun acknowledge() {
        if (isHandled) {
            return
        }
        isHandled = true
        eventClient.trackInteraction(ad)
    }

    @Synchronized
    override fun itemAcknowledge(item: AddToListItem) {
        if (!isHandled) {
            isHandled = true
            eventClient.trackInteraction(ad)
        }
        trackItem(item.title)
    }

    @Synchronized
    private fun trackItem(itemName: String) {
        val params: MutableMap<String, String> = HashMap()
        params[AD_ID] = ad.id
        params[ITEM_NAME] = itemName
        //appEventClient.trackSdkEvent(EventStrings.ATL_ITEM_ADDED_TO_LIST, params)
    }

    @Synchronized
    override fun failed(message: String) {
        if (isHandled) {
            return
        }
        isHandled = true
        val params: MutableMap<String, String> = HashMap()
        params[AD_ID] = ad.id
//        appEventClient.trackError(
//            EventStrings.ATL_ADDED_TO_LIST_FAILED,
//            if (message.isEmpty()) UNKNOWN_REASON else message,
//            params
//        )
    }

    override fun itemFailed(item: AddToListItem, message: String) {
        isHandled = true
        val params: MutableMap<String, String> = HashMap()
        params[AD_ID] = ad.id
        params[ITEM] = item.title
//        appEventClient.trackError(
//            EventStrings.ATL_ADDED_TO_LIST_ITEM_FAILED,
//            if (message.isEmpty()) UNKNOWN_REASON else message,
//            params
//        )
    }

    val zoneId: String
        get() = ad.zoneId

    override fun getItems(): List<AddToListItem> {
        return items
    }

    override fun hasNoItems(): Boolean {
        return items.isEmpty()
    }

    override fun getSource(): String {
        return AddToListContent.Sources.IN_APP
    }

    companion object {
        private const val ADD_TO_LIST = 0
        private const val AD_ID = "ad_id"
        private const val ITEM_NAME = "item_name"
        private const val ITEM = "item"
        private const val UNKNOWN_REASON = "Unknown Reason"

        fun createAddToListContent(ad: Ad): AdContent {
            return AdContent(ad, ADD_TO_LIST, ad.payload.detailedListItems)
        }
    }

    init {
        if (ad.payload.detailedListItems.isEmpty()) {
            //appClient.trackError(EventStrings.AD_PAYLOAD_IS_EMPTY, "Ad ${ad.id} has empty payload")
        }
        isHandled = false
    }
}