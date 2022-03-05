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
    private var listener: Listener? = null
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
        this.listener = listener
        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    fun onStop(contentListener: AdContentListener? = null) {
        this.listener = null
        presenter?.onDetach()
        if (contentListener != null) {
            AdContentPublisher.getInstance().removeListener(contentListener)
        }
    }

    private fun notifyZoneHasAds(hasAds: Boolean) {
        Handler(Looper.getMainLooper()).post {
            listener?.onZoneHasAds(hasAds)
        }
    }

    private fun notifyAdLoaded() {
        Handler(Looper.getMainLooper()).post {
            listener?.onAdLoaded()
        }
    }

    private fun notifyAdLoadFailed() {
        Handler(Looper.getMainLooper()).post {
            listener?.onAdLoadFailed()
        }
    }

    override fun onZoneAvailable(zone: Zone) {
        var adjustedLayoutParams = LayoutParams(width, height)
        if (width == 0 || height == 0) {
            val dimension = zone.dimensions[Dimension.Orientation.PORT] //todo wtf is this all about
            adjustedLayoutParams = LayoutParams(
                dimension?.width ?: LayoutParams.MATCH_PARENT,
                dimension?.height ?: LayoutParams.MATCH_PARENT
            )

            //adjustedLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 120)
        }
        Handler(Looper.getMainLooper()).post { webView.layoutParams = adjustedLayoutParams }
        notifyZoneHasAds(zone.hasAds())
    }

    override fun onAdsRefreshed(zone: Zone) {
        notifyZoneHasAds(zone.hasAds())
    }

    override fun onAdAvailable(ad: Ad) {
        if (isVisible) {
            Handler(Looper.getMainLooper()).post { webView.loadAd(ad) }
        }
    }

    override fun onNoAdAvailable() {
        Handler(Looper.getMainLooper()).post { webView.loadBlank() }
    }

    override fun onAdLoaded(ad: Ad) {
        if (presenter != null) {
            presenter?.onAdDisplayed(ad, isAdVisible)
            notifyAdLoaded()
        }
    }

    override fun onAdLoadFailed() {
        if (presenter != null) {
            presenter?.onAdDisplayFailed()
            notifyAdLoadFailed()
        }
    }

    override fun onAdClicked(ad: Ad) {
        presenter?.onAdClicked(ad)
    }

    override fun onBlankLoaded() {
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
