package com.adadapted.library.interfaces

import com.adadapted.library.session.Session

interface AdGetListener {
    fun onNewAdsLoaded(session: Session)
    fun onNewAdsLoadFailed()
}