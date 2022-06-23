package com.adadapted.library.view

import android.webkit.JavascriptInterface
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContent.Companion.createAddToListContent
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.atl.PopupContent.Companion.createPopupContent
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient

class JavascriptBridge internal constructor(private val ad: Ad) {
    @JavascriptInterface
    fun deliverAdContent() {
        val params = HashMap<String, String>()
        params["ad_id"] = ad.id
        EventClient.trackSdkEvent(EventStrings.POPUP_CONTENT_CLICKED, params)
        val content = createAddToListContent(ad)
        AddItContentPublisher.publishAdContent(content)
    }

    @JavascriptInterface
    fun addItemToList(
        payloadId: String,
        trackingId: String,
        title: String,
        brand: String,
        category: String,
        barCode: String,
        retailerSku: String,
        discount: String,
        productImage: String
    ) {
        val params = HashMap<String, String>()
        params["tracking_id"] = trackingId
        EventClient.trackSdkEvent(EventStrings.POPUP_ATL_CLICKED, params)

        val items: MutableList<AddToListItem> = ArrayList()
        items.add(
            AddToListItem(
                trackingId,
                title,
                brand,
                category,
                barCode,
                retailerSku,
                discount,
                productImage
            )
        )
        val content = createPopupContent(payloadId, items)
        AddItContentPublisher.publishPopupContent(content)
    }
}