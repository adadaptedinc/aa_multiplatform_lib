package com.adadapted.library.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.session.SessionClient

class AndroidZoneView : FrameLayout, AdZonePresenterListener, AndroidWebView.Listener {
    interface Listener {
        fun onZoneHasAds(hasAds: Boolean)
        fun onAdLoaded()
        fun onAdLoadFailed()
    }

    private lateinit var webView: AndroidWebView
    private var presenter: AdZonePresenter = AdZonePresenter(AdViewHandler(context), SessionClient)
    private var zoneViewListener: Listener? = null
    private var isVisible = true
    private var isAdVisible = true

    constructor(context: Context) : super(context.applicationContext) {
        setup(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context.applicationContext, attrs) {
        setup(context)
    }

    private fun setup(context: Context) {
        webView = AndroidWebView(context.applicationContext, this)
        Handler(Looper.getMainLooper()).post { addView(webView) }
    }

    fun init(zoneId: String) {
        presenter.init(zoneId)
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter.onAdVisibilityChanged(isAdVisible)
    }

    fun onStart(listener: Listener? = null, contentListener: AdContentListener? = null) {
        presenter.onAttach(this)
        this.zoneViewListener = listener
        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    fun onStop(contentListener: AdContentListener? = null) {
        this.zoneViewListener = null
        presenter.onDetach()
        if (contentListener != null) {
            AdContentPublisher.getInstance().removeListener(contentListener)
        }
    }

    private fun notifyClientZoneHasAds(hasAds: Boolean) {
        Handler(Looper.getMainLooper()).post {
            zoneViewListener?.onZoneHasAds(hasAds)
        }
    }

    private fun notifyClientAdLoaded() {
        Handler(Looper.getMainLooper()).post {
            zoneViewListener?.onAdLoaded()
        }
    }

    private fun notifyClientAdLoadFailed() {
        Handler(Looper.getMainLooper()).post {
            zoneViewListener?.onAdLoadFailed()
        }
    }

    override fun onZoneAvailable(zone: Zone) {
        var adjustedLayoutParams = LayoutParams(width, height)
        if (width == 0 || height == 0) {
            adjustedLayoutParams = LayoutParams(
                if (zone.portWidth < 1) { ViewGroup.LayoutParams.MATCH_PARENT } else { zone.portWidth.toInt() },
                if (zone.portHeight < 1) { ViewGroup.LayoutParams.MATCH_PARENT } else { zone.portHeight.toInt() }
            )
        }
        Handler(Looper.getMainLooper()).post { webView.layoutParams = adjustedLayoutParams }
        notifyClientZoneHasAds(zone.hasAds())
    }

    override fun onAdsRefreshed(zone: Zone) {
        notifyClientZoneHasAds(zone.hasAds())
    }

    override fun onAdAvailable(ad: Ad) {
        if (isVisible) {
            Handler(Looper.getMainLooper()).post { webView.loadAd(ad) }
        }
    }

    override fun onNoAdAvailable() {
        Handler(Looper.getMainLooper()).post { webView.loadBlank() }
    }

    override fun onAdLoadedInWebView(ad: Ad) {
        presenter.onAdDisplayed(ad, isAdVisible)
        notifyClientAdLoaded()
    }

    override fun onAdLoadInWebViewFailed() {
        presenter.onAdDisplayFailed()
        notifyClientAdLoadFailed()
    }

    override fun onAdInWebViewClicked(ad: Ad) {
        presenter.onAdClicked(ad)
    }

    override fun onBlankAdInWebViewLoaded() {
        presenter.onBlankDisplayed()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            View.GONE -> setInvisible()
            View.INVISIBLE -> setInvisible()
            View.VISIBLE -> setVisible()
        }
    }

    private fun setVisible() {
        isVisible = true
        presenter.onAttach(this)
    }

    private fun setInvisible() {
        isVisible = false
        presenter.onDetach()
    }
}
