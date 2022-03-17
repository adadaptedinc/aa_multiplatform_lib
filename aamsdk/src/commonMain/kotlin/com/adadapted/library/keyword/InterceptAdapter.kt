package com.adadapted.library.keyword

import com.adadapted.library.session.Session

interface InterceptAdapter {
    interface Callback {
        fun onSuccess(intercept: Intercept)
    }
    fun retrieve(session: Session, callback: Callback)
    fun sendEvents(session: Session, events: MutableSet<InterceptEvent>)
}