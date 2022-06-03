package com.adadapted.library.interfaces

import com.adadapted.library.event.AdEvent

interface EventClientListener {
    fun onAdEventTracked(event: AdEvent?)
}
