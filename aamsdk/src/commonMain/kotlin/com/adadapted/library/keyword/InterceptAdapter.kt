package com.adadapted.library.keyword

import com.adadapted.library.session.Session

interface InterceptAdapter {
    interface Listener {
        fun onSuccess(intercept: Intercept)
    }

    suspend fun retrieve(session: Session, listener: Listener)
    suspend fun sendEvents(session: Session, events: MutableSet<InterceptEvent>)
}