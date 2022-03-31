package com.adadapted.library.view

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.concurrency.Transporter
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

interface ZoneViewListener {
    fun onZoneHasAds(hasAds: Boolean)
    fun onAdLoaded()
    fun onAdLoadFailed()
}

class IosZoneView : UIView(frame = cValue { CGRectZero }),
    UIViewWithOverridesProtocol,
    UIGestureRecognizerDelegateProtocol {

    private var webView: IosWebView = IosWebView.getInstance()
    private var presenter: AdZonePresenter = AdZonePresenter(AdViewHandler())
    private var zoneViewListener: ZoneViewListener? = null
    private var isVisible = true
    private var isAdVisible = true

    init {
        this.webView.addWebViewListener(setWebViewListener())
        addSubview(webView)
        setupConstraints()
    }

    fun shutdown() {
        this.onStop()
    }

    fun setAdZoneVisibility(isViewable: Boolean) {
        isAdVisible = isViewable
        presenter.onAdVisibilityChanged(isAdVisible)
    }

    fun onStart(
        zoneViewListener: ZoneViewListener? = null,
        contentListener: AdContentListener? = null
    ) {
        println("onStart")
        println("zoneViewListener: $zoneViewListener")
        presenter.onAttach(setAdZonePresenterListener())
        this.zoneViewListener = zoneViewListener
        println("zoneViewListener: $zoneViewListener")
        if (contentListener != null) {
            AdContentPublisher.getInstance().addListener(contentListener)
        }
    }

    override fun layoutSubviews() {
        println("laying out ZoneView subviews")
        setNeedsDisplay()
    }

    override fun drawRect(aRect: CValue<CGRect>) {
        val rectAsString = aRect.useContents {
            "" + this.origin.x + ", " + this.origin.y + ", " + (this.origin.x + this.size.width) + ", " + (this.origin.y + this.size.height)
        }
        println("ZoneView dimensions: $rectAsString")
        // need to draw to screen here
        webView.loadAd(Ad())
    }

    private fun setWebViewListener() = object : WebViewListener {
        override fun onAdLoadedInWebView(ad: Ad) {
            println("Ad loaded in webview: $ad")
            presenter.onAdDisplayed(ad, isAdVisible)
            notifyClientAdLoaded()
        }

        override fun onAdLoadInWebViewFailed() {
            println("Ad load in webview failed")
            presenter.onAdDisplayFailed()
            notifyClientAdLoadFailed()
        }

        override fun onAdInWebViewClicked(ad: Ad) {
            println("Ad in webview clicked: $ad")
            presenter.onAdClicked(ad)
        }

        override fun onBlankAdInWebViewLoaded() {
            println("Blank ad in webview loaded")
            presenter.onBlankDisplayed()
        }
    }

    private fun setAdZonePresenterListener() = object : AdZonePresenterListener {
        override fun onZoneAvailable(zone: Zone) {
            println("Zone available: $zone")
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdsRefreshed(zone: Zone) {
            println("Ads refrehed for zone: $zone")
            notifyClientZoneHasAds(zone.hasAds())
        }

        override fun onAdAvailable(ad: Ad) {
            println("Ad available: $ad")
            if (isVisible) {
                Transporter().dispatchToMain {
                    println("onAdAvailalbe ad: $ad")
                    webView.loadAd(ad)
                    drawRect(cValue { CGRectZero })
                }
            }
        }

        override fun onNoAdAvailable() {
            println("No ad available")
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
        constraints.add(webView.leadingAnchor.constraintEqualToAnchor(leadingAnchor))
        constraints.add(webView.trailingAnchor.constraintEqualToAnchor(trailingAnchor))
        constraints.add(webView.heightAnchor.constraintEqualToAnchor(heightAnchor))
        constraints.add(webView.widthAnchor.constraintEqualToAnchor(widthAnchor))
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

    companion object {
        fun getInstance(): IosZoneView {
            val zoneView = IosZoneView()
            zoneView.onStart()
            return zoneView
        }
    }
}

fun createZoneView(): UIView = IosZoneView.getInstance()

fun UIView.constraintsToFillSuperview(): List<NSLayoutConstraint> {
    val horizontal = constraintsToFillSuperviewHorizontally()
    val vertical = constraintsToFillSuperviewVertically()
    return vertical + horizontal
}

fun UIView.constraintsToFillSuperviewVertically(): List<NSLayoutConstraint> {
    val superview = superview ?: return emptyList()
    val top = topAnchor.constraintEqualToAnchor(superview.topAnchor)
    val bottom = bottomAnchor.constraintEqualToAnchor(superview.bottomAnchor)
    return listOf(top, bottom)
}

fun UIView.constraintsToFillSuperviewHorizontally(): List<NSLayoutConstraint> {
    val superview = superview ?: return emptyList()
    val leader = leadingAnchor.constraintEqualToAnchor(superview.leadingAnchor)
    val trailer = trailingAnchor.constraintEqualToAnchor(superview.trailingAnchor)
    return listOf(leader, trailer)
}
