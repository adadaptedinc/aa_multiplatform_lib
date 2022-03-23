package com.adadapted.library.view

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.concurrency.Transporter
import kotlinx.cinterop.*
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

interface ZoneViewListener {
    fun onZoneHasAds(hasAds: Boolean)
    fun onAdLoaded()
    fun onAdLoadFailed()
}

class IosZoneView() : UIView(frame = cValue { CGRectZero }), UIViewWithOverridesProtocol {

    private var webView: IosWebView = IosWebView.getInstance()
    private var presenter: AdZonePresenter = AdZonePresenter(AdViewHandler())
    private var zoneViewListener: ZoneViewListener? = null
    private var isVisible = true
    private var isAdVisible = true

    init {
        this.webView.addWebViewListener(setWebViewListener())
        setupConstraints()
        addSubview(webView)
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter.onAdVisibilityChanged(isAdVisible)
    }

    override fun layoutSubviews() {
        println("laying out ZoneView subviews")
        setNeedsDisplay()
    }

    override fun drawRect(aRect: CValue<CGRect>) {
        val rectAsString = aRect.useContents {
            "" + this.origin.x + ", " + this.origin.y + ", " + (this.origin.x +this.size.width) + ", " + (this.origin.y +this.size.height)
        }
        println("ZoneView dimensions: $rectAsString")
    }

    private fun onStart(zoneViewListener: ZoneViewListener, contentListener: AdContentListener? = null) {
        presenter.onAttach(setAdZonePresenterListener())
        this.zoneViewListener = zoneViewListener
        println("zoneViewListener: $zoneViewListener")
        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    private fun setWebViewListener() = object: WebViewListener {
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
    }

    private fun setAdZonePresenterListener() = object: AdZonePresenter.Listener {
        override fun onZoneAvailable(zone: Zone) {
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdsRefreshed(zone: Zone) {
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdAvailable(ad: Ad) {
            if (isVisible) {
                Transporter().dispatchToMain {
                    println("onAdAvailalbe ad: $ad")
                    webView.loadAd(ad)
                    drawRect(cValue { CGRectZero })
                }
            }
        }

        override fun onNoAdAvailable() {
            Transporter().dispatchToMain {
                webView.loadBlank()
                drawRect(cValue { CGRectZero })
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
            println("notifyClientAdLoaded")
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
}

fun createZoneView(): UIView = IosZoneView()
