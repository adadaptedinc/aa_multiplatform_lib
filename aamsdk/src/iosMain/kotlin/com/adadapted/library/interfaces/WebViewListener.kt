package com.adadapted.library.interfaces

import com.adadapted.library.ad.Ad

interface WebViewListener {
    fun onAdLoadedInWebView(ad: Ad)
    fun onAdLoadInWebViewFailed()
    fun onAdInWebViewClicked(ad: Ad)
    fun onBlankAdInWebViewLoaded()
}