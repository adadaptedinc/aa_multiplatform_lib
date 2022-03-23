package com.adadapted.library.view

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import com.adadapted.library.ad.Ad
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import platform.WebKit.*

interface WebViewListener {
    fun onAdLoadedInWebView(ad: Ad)
    fun onAdLoadInWebViewFailed()
    fun onAdInWebViewClicked(ad: Ad)
    fun onBlankAdInWebViewLoaded()
}

class IosWebView constructor(): WKWebView (frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration()),
        WKUIDelegateProtocol,
        WKNavigationDelegateProtocol,
        UIViewWithOverridesProtocol {

    private lateinit var currentAd: Ad
    private var listener: WebViewListener? = null
    private var loaded = false
    private val presenterListener: AdZonePresenter.Listener? = null

    init {
        setUIDelegate(this)
        setNavigationDelegate(this)
        this.translatesAutoresizingMaskIntoConstraints = false
        this.setAllowsBackForwardNavigationGestures(false)
        this.scrollView().bounces = false
        this.scrollView().setContentInsetAdjustmentBehavior(UIScrollViewContentInsetAdjustmentBehavior.UIScrollViewContentInsetAdjustmentNever)
        setupConstraints()
    }

    fun addWebViewListener(listener: WebViewListener) {
        this.listener = listener
    }

    fun loadAd(ad: Ad) {
        currentAd = ad
        loaded = false
        val url = NSURL(string = "https://sandbox.adadapted.com/a/NWY0NTZIODZHNWY0;101942;45445")
        val request = NSURLRequest(uRL = url)
        loadRequest(request)

        println("url = $url")
        println("ad: $ad")
    }

    fun loadBlank() {
        currentAd = Ad()
        val dummyDocument =
            "<html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head><body></body></html>"
        val data = dummyDocument.nsData()
        if (data != null) {
            loadData(data, "text/html", null.toString(), NSURL(string = ""))
        }
//        notifyBlankLoaded()
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

    private fun notifyAdLoaded() {
        listener?.onAdLoadedInWebView(currentAd)
    }

    private fun notifyAdLoadFailed() {
        listener?.onAdLoadInWebViewFailed()
    }

    private fun notifyBlankLoaded() {
        listener?.onBlankAdInWebViewLoaded()
    }

    private fun notifyAdClicked() {
        listener?.onAdInWebViewClicked(currentAd)
    }

    private fun setupConstraints() {
        val constraints = mutableListOf<NSLayoutConstraint>()
        constraints += this.constraintsToFillSuperview()
        NSLayoutConstraint.activateConstraints(constraints)
    }

    // WKWebView Navigation Delegate
    override fun webView(webView: WKWebView, decidePolicyForNavigationAction: WKNavigationAction, decisionHandler: (WKNavigationActionPolicy) -> Unit) {
        if (decidePolicyForNavigationAction.navigationType == WKNavigationTypeOther) {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        } else {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
        }
    }

    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        if ((currentAd.id.isNotEmpty() == true)  && !loaded) {
            loaded = true
//            notifyAdLoaded()
        }
    }

    override fun webView(webView: WKWebView, didFailNavigation: WKNavigation?, withError: NSError) {
        if ((currentAd.id.isNotEmpty() == true) && !loaded) {
            loaded = true
//            notifyAdLoadFailed()
        }
    }

    @ThreadLocal
    companion object {
        fun getInstance(): IosWebView {
            return IosWebView()
        }
    }
}

fun String.nsData(): NSData? =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

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
