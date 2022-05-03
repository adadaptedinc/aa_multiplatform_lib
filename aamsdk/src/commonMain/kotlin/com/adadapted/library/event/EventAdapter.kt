package com.adadapted.library.event

import com.adadapted.library.session.Session

interface EventAdapter {
    suspend fun publishAdEvents(session: Session, adEvents: Set<AdEvent>)
}