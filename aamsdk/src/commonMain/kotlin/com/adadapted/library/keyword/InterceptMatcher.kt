package com.adadapted.library.keyword

import com.adadapted.library.constants.EventStrings
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import kotlin.native.concurrent.ThreadLocal

class InterceptMatcher private constructor() : SessionListener, InterceptClient.Listener {
    private var intercept: Intercept = Intercept()
    private var mLoaded = false

    private fun matchKeyword(constraint: CharSequence): Set<Suggestion> {
        val suggestions: MutableSet<Suggestion> = HashSet()
        val input = constraint.toString()
        if (!shouldCheckConstraint(input)) {
            return suggestions
        }
        for (interceptTerm in intercept.getTerms()) {
            if (interceptTerm.term.startsWith(input, ignoreCase = true)) {
                fileTerm(interceptTerm, constraint.toString(), suggestions)
                break
            }
        }
        if (suggestions.isEmpty()) {
            SuggestionTracker.suggestionNotMatched(intercept.searchId, constraint.toString())
        }
        return suggestions
    }

    private fun fileTerm(term: Term?, input: String, suggestions: MutableSet<Suggestion>) {
        if (term != null) {
            suggestions.add(Suggestion(intercept.searchId, term))
            SuggestionTracker.suggestionMatched(
                intercept.searchId,
                term.termId,
                term.term,
                term.replacement,
                input
            )
        }
    }

    private fun shouldCheckConstraint(input: String?): Boolean {
        return isLoaded && input != null && input.length >= intercept.minMatchLength
    }

    private val isLoaded: Boolean
        get() {
            return mLoaded
        }

    override fun onKeywordInterceptInitialized(intercept: Intercept) {
        //AppEventClient.getInstance().trackSdkEvent(EventStrings.KI_INITIALIZED)
        this.intercept = intercept
        mLoaded = true
    }

    override fun onSessionAvailable(session: Session) {
        if (session.id.isNotEmpty()) {
            InterceptClient.getInstance().initialize(session, this)
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: InterceptMatcher

        fun match(constraint: CharSequence): Set<Suggestion> {
            return if (this::instance.isInitialized) {
                instance.matchKeyword(constraint)
            } else {
                when {
                    SessionClient.hasInstance() -> {
                        instance = InterceptMatcher()
                        emptySet()
                    }
                    else -> {
                        return emptySet()
                    }
                }
            }
        }
    }

    init {
        SessionClient.getInstance().addListener(this)
    }
}