package com.adadapted.library.payload

import kotlinx.datetime.Clock

class PayloadEvent internal constructor(val payloadId: String, val status: String) {
    val timestamp: Long = Clock.System.now().epochSeconds
}