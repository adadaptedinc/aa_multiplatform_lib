package com.adadapted.library.interfaces

import com.adadapted.library.session.Session

interface SessionInitListener {
    fun onSessionInitialized(session: Session)
    fun onSessionInitializeFailed()
}