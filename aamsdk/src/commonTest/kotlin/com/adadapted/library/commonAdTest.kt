package com.adadapted.library

import com.adadapted.library.ad.Ad
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.payload.Payload
import kotlin.test.*

class CommonAdTest {

    private var testAd = Ad(
        id = "testAdId", impressionId = "123456:00000", "urlString", "atl", "a", Payload(
            "payloadId", "pMsg", "pImg", "campaignId", "appId", 60, listOf(
                AddToListItem(
                    "trackingId",
                    "testTitle",
                    "testBrand",
                    "testCat",
                    "productUpc",
                    "retailSku",
                    "retailId",
                    "prodImg"
                )
            )
        ), 30
    )

    @Test
    fun adZoneIsSplitCorrectly() {
        assertEquals("123456", testAd.zoneId)
    }

    @Test
    fun emptyAdIdReturnsEmpty() {
        val emptyAd = Ad()
        assertTrue { emptyAd.isEmpty }
    }

    @Test
    fun adImpressionSetsTracking() {
        testAd.setImpressionTracked()
        assertTrue(testAd.impressionWasTracked())
        testAd.resetImpressionTracking()
        assertFalse(testAd.impressionWasTracked())
    }

    @Test
    fun adContentIsCreatedProperly() {
        val testAdContent = testAd.getContent()
        assertEquals("testTitle", testAdContent.getItems().first().title)
        assertEquals("123456", testAdContent.zoneId)
    }
}