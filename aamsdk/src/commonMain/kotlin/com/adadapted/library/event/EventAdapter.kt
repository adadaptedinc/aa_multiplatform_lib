package com.adadapted.library.event

import com.adadapted.library.session.Session

interface EventAdapter {
    suspend fun sendAdEvents(session: Session, adEvents: Set<AdEvent>)
}