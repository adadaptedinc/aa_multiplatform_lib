package com.adadapted.library.interfaces

import com.adadapted.library.keyword.Intercept

interface InterceptListener {
    fun onKeywordInterceptInitialized(intercept: Intercept)
}