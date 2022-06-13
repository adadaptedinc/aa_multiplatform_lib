package com.adadapted.library

import com.adadapted.library.ad.Ad
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonAdTest {

    @Test
    fun testAdExample() {
        val testAd = Ad()
        assertTrue(testAd.isEmpty)
    }

    @Test
    fun testAdExample2() {
        val testAd = Ad()
        testAd.setImpressionTracked()
        assertTrue(testAd.impressionWasTracked())
        testAd.resetImpressionTracking()
        assertFalse(testAd.impressionWasTracked())
    }
}