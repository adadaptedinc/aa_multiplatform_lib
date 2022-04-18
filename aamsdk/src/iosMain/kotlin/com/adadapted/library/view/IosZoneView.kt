package com.adadapted.library.view

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import com.adadapted.library.interfaces.ZoneViewListener
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.constraintsToFillSuperview
import com.adadapted.library.interfaces.WebViewListener
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

class IosZoneView : UIView(frame = cValue { CGRectZero }),
    UIViewWithOverridesProtocol {

    private var webView: IosWebView = IosWebView()
    var presenter: AdZonePresenter = AdZonePresenter(AdViewHandler())
    var zoneViewListener: ZoneViewListener? = null
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

    fun onStart(
        zoneViewListener: ZoneViewListener? = null,
        contentListener: AdContentListener? = null
    ) {
        presenter.onAttach(setAdZonePresenterListener())
        this.zoneViewListener = zoneViewListener

        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter.onAdVisibilityChanged(isAdVisible)
    }

    private fun setWebViewListener() = object : WebViewListener {
        override fun onAdLoadedInWebView(ad: Ad) {
            println(LOG_TAG + "Ad loaded: $ad")
            presenter.onAdDisplayed(ad, isAdVisible)
            notifyClientAdLoaded()
        }

        override fun onAdLoadInWebViewFailed() {
            println(LOG_TAG + "Ad load failed")
            presenter.onAdDisplayFailed()
            notifyClientAdLoadFailed()
        }

        override fun onAdInWebViewClicked(ad: Ad) {
            println("Ad clicked: $ad")
            presenter.onAdClicked(ad)
        }

        override fun onBlankAdInWebViewLoaded() {
            println(LOG_TAG + "Blank ad loaded")
            presenter.onBlankDisplayed()
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
                Transporter().dispatchToMain {
                    webView.loadAd(ad)
                    setNeedsDisplay()
                }
            }
        }

        override fun onNoAdAvailable() {
            Transporter().dispatchToMain {
                webView.loadBlank()
                setNeedsDisplay()
            }
        }
    }

    private fun onStop(contentListener: AdContentListener? = null) {
        this.zoneViewListener = null
        presenter.onDetach()
        if (contentListener != null) {
            AdContentPublisher.getInstance().removeListener(contentListener)
        }
    }

    private fun notifyClientZoneHasAds(hasAds: Boolean) {
        Transporter().dispatchToMain {
            zoneViewListener?.onZoneHasAds(hasAds)
        }
    }

    private fun notifyClientAdLoaded() {
        Transporter().dispatchToMain {
            zoneViewListener?.onAdLoaded()
        }
    }

    private fun notifyClientAdLoadFailed() {
        Transporter().dispatchToMain {
            zoneViewListener?.onAdLoadFailed()
        }
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

    private fun setVisible() {
        isVisible = true
        presenter.onAttach(setAdZonePresenterListener())
    }

    private fun setInvisible() {
        isVisible = false
        presenter.onDetach()
    }

    override fun layoutSubviews() {
        println(LOG_TAG + "Laying out ZoneView")
        setNeedsDisplay()
    }

    override fun drawRect(aRect: CValue<CGRect>) {
        val rectAsString = aRect.useContents {
            "" + this.origin.x + ", " + this.origin.y + ", " + (this.origin.x + this.size.width) + ", " + (this.origin.y + this.size.height)
        }
        println(LOG_TAG + "AAZoneView dimensions: $rectAsString")
    }
}
