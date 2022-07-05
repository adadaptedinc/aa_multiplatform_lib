package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constraintsToFillSuperview
import com.adadapted.library.interfaces.WebViewListener
import com.adadapted.library.interfaces.ZoneViewListener
import com.adadapted.library.log.AALogger
import com.adadapted.library.session.SessionClient
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

class IosZoneView : UIView(frame = cValue { CGRectZero }) {

    var presenter: AdZonePresenter = AdZonePresenter(AdViewHandler(), SessionClient)
    var zoneViewListener: ZoneViewListener? = null
    private var webView: IosWebView = IosWebView()
    private var isVisible = true
    private var isAdVisible = true

    init {
        this.webView.addWebViewListener(setWebViewListener())
        addSubview(webView)
        setupConstraints()
    }

    fun initZone(zoneId: String) {
        this.presenter.init(zoneId)
    }

    fun setZoneViewListener(listener: ZoneViewListener? = null) {
        presenter.onAttach(setAdZonePresenterListener())
        this.zoneViewListener = listener
    }

    fun setAdContentListener(listener: AdContentListener? = null) {
        if (listener != null) {
            AdContentPublisher.addListener(listener)
        }
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter.onAdVisibilityChanged(isAdVisible)
    }

    private fun onStop(contentListener: AdContentListener? = null) {
        this.zoneViewListener = null
        presenter.onDetach()
        if (contentListener != null) {
            AdContentPublisher.removeListener(contentListener)
        }
    }

    private fun notifyClientZoneHasAds(hasAds: Boolean) {
        Transporter().dispatchToThread {
            zoneViewListener?.onZoneHasAds(hasAds)
        }
    }

    private fun notifyClientAdLoaded() {
        Transporter().dispatchToThread {
            zoneViewListener?.onAdLoaded()
        }
    }

    private fun notifyClientAdLoadFailed() {
        Transporter().dispatchToThread {
            zoneViewListener?.onAdLoadFailed()
        }
    }

    private fun setAdZonePresenterListener() = object : AdZonePresenterListener {
        override fun onZoneAvailable(zone: Zone) {
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdsRefreshed(zone: Zone) {
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdAvailable(ad: Ad) {
            if (isVisible) {
                Transporter().dispatchToThread {
                    webView.loadAd(ad)
                    setNeedsDisplay()
                }
            }
        }

        override fun onNoAdAvailable() {
            Transporter().dispatchToThread {
                webView.loadBlank()
                setNeedsDisplay()
            }
        }
    }

    private fun setWebViewListener() = object : WebViewListener {
        override fun onAdLoadedInWebView(ad: Ad) {
            AALogger.logInfo("Ad ${ad.id} loaded")
            presenter.onAdDisplayed(ad, isAdVisible)
            notifyClientAdLoaded()
        }

        override fun onAdLoadInWebViewFailed() {
            AALogger.logInfo("Ad load failed")
            presenter.onAdDisplayFailed()
            notifyClientAdLoadFailed()
        }

        override fun onAdInWebViewClicked(ad: Ad) {
            AALogger.logInfo("ad ${ad.id} interaction")
            presenter.onAdClicked(ad)
        }

        override fun onBlankAdInWebViewLoaded() {
            AALogger.logInfo("Blank ad loaded")
            presenter.onBlankDisplayed()
        }
    }

    private fun setVisible() {
        isVisible = true
        presenter.onAttach(setAdZonePresenterListener())
    }

    private fun setInvisible() {
        isVisible = false
        presenter.onDetach()
    }

    private fun setupConstraints() {
        val constraints = mutableListOf<NSLayoutConstraint>()
        constraints += this.constraintsToFillSuperview()
        constraints.add(webView.leadingAnchor.constraintEqualToAnchor(leadingAnchor))
        constraints.add(webView.trailingAnchor.constraintEqualToAnchor(trailingAnchor))
        constraints.add(webView.heightAnchor.constraintEqualToAnchor(heightAnchor))
        constraints.add(webView.widthAnchor.constraintEqualToAnchor(widthAnchor))
        constraints.add(webView.centerXAnchor.constraintEqualToAnchor(centerXAnchor))
        NSLayoutConstraint.activateConstraints(constraints)
    }
}
