package com.adadapted.library.network

import com.adadapted.library.keyword.InterceptAdapter
import com.adadapted.library.keyword.InterceptEvent
import com.adadapted.library.session.Session

class HttpInterceptAdapter(private val initUrl: String, private val eventUrl: String) :
    HttpConnector(), InterceptAdapter {

    override fun retrieve(session: Session, callback: InterceptAdapter.Callback) {
        TODO("Not yet implemented")
    }

    override fun sendEvents(session: Session, events: MutableSet<InterceptEvent>) {
        TODO("Not yet implemented")
    }
}