package com.adadapted.library

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.dimension.Dimension
import com.adadapted.library.view.AdZonePresenter
import com.adadapted.library.view.Zone

class AndroidZoneView : RelativeLayout, AdZonePresenter.Listener, AndroidWebView.Listener {
    interface Listener {
        fun onZoneHasAds(hasAds: Boolean)
        fun onAdLoaded()
        fun onAdLoadFailed()
    }

    private lateinit var webView: AndroidWebView
    private var presenter: AdZonePresenter? = null
    private var isVisible = true
    private var zoneViewListener: Listener? = null
    private var isAdVisible = true

    constructor(context: Context) : super(context.applicationContext) {
        setup(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context.applicationContext, attrs) {
        setup(context)
    }

    private fun setup(context: Context) {
        presenter = AdZonePresenter()
        webView = AndroidWebView(context.applicationContext, this)
        Handler(Looper.getMainLooper()).post { addView(webView) }
    }

    fun init(zoneId: String) {
        presenter?.init(zoneId)
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter?.onAdVisibilityChanged(isAdVisible)
    }

    fun onStart(listener: Listener? = null, contentListener: AdContentListener? = null) {
        presenter?.onAttach(this)
        this.zoneViewListener = listener
        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    fun onStop(contentListener: AdContentListener? = null) {
        this.zoneViewListener = null
        presenter?.onDetach()
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
            val dimension = zone.dimensions[Dimension.Orientation.PORT] //todo wtf is this all about
            adjustedLayoutParams = LayoutParams(
                dimension?.width ?: LayoutParams.MATCH_PARENT,
                dimension?.height ?: LayoutParams.WRAP_CONTENT
            )

            //adjustedLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 120)
        }
        Handler(Looper.getMainLooper()).post { webView.layoutParams = adjustedLayoutParams }
        notifyClientZoneHasAds(zone.hasAds())
        println("AADEBUG" + "AAZONEVIEW onZoneAvailable HIT AND ZONE MADE")
    }

    override fun onAdsRefreshed(zone: Zone) {
        notifyClientZoneHasAds(zone.hasAds())
    }

    override fun onAdAvailable(ad: Ad) {
        println("AADEBUG" + "AAZONEVIEW onAdAvailable HIT AND webview loading")
        if (isVisible) {
            Handler(Looper.getMainLooper()).post { webView.loadAd(ad) }
        }
    }

    override fun onNoAdAvailable() {
        Handler(Looper.getMainLooper()).post { webView.loadBlank() }
    }

    override fun onAdLoadedInWebView(ad: Ad) {
        if (presenter != null) {
            presenter?.onAdDisplayed(ad, isAdVisible)
            notifyClientAdLoaded()
        }
    }

    override fun onAdLoadInWebViewFailed() {
        if (presenter != null) {
            presenter?.onAdDisplayFailed()
            notifyClientAdLoadFailed()
        }
    }

    override fun onAdInWebViewClicked(ad: Ad) {
        presenter?.onAdClicked(ad)
    }

    override fun onBlankAdInWebViewLoaded() {
        presenter?.onBlankDisplayed()
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
        presenter?.onAttach(this)
    }

    private fun setInvisible() {
        isVisible = false
        presenter?.onDetach()
    }
}
