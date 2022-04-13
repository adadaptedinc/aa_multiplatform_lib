package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.interfaces.WebViewListener
import com.adadapted.library.nsData
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import platform.WebKit.*

class IosWebView : WKWebView (frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration()),
        WKUIDelegateProtocol,
        WKNavigationDelegateProtocol,
        UIGestureRecognizerDelegateProtocol {

    private lateinit var currentAd: Ad
    private var listener: WebViewListener? = null
    private var loaded = false

    init {
        setUIDelegate(this)
        setNavigationDelegate(this)
        this.translatesAutoresizingMaskIntoConstraints = false
        this.setAllowsBackForwardNavigationGestures(false)
        this.scrollView().bounces = false
        this.scrollView()
            .setContentInsetAdjustmentBehavior(UIScrollViewContentInsetAdjustmentBehavior.UIScrollViewContentInsetAdjustmentNever)

        this.setOnCLickListener { tapAction() }
    }

    fun addWebViewListener(listener: WebViewListener) {
        this.listener = listener
    }

    fun loadAd(ad: Ad) {
        currentAd = ad
        loaded = false
        val url = NSURL(string = currentAd.url )
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
        notifyBlankLoaded()
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

    // WKWebView Navigation Delegate
    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        if (decidePolicyForNavigationAction.navigationType == WKNavigationTypeOther) {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        } else {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
        }
    }

    override fun webView(
        webView: WKWebView,
        didFinishNavigation: WKNavigation?
    ) {
        if (currentAd.id.isNotEmpty() && !loaded) {
            loaded = true
            notifyAdLoaded()
        }
    }

    override fun webView(
        webView: WKWebView,
        didFailNavigation: WKNavigation?,
        withError: NSError
    ) {
        if (currentAd.id.isNotEmpty() && !loaded) {
            loaded = true
            notifyAdLoadFailed()
        }
    }

    override fun webView(
        webView: WKWebView,
        contextMenuWillPresentForElement: WKContextMenuElementInfo
    ) {
        // NOOP
    }

    // Gesture Recognizer Delegate
    private fun UIView.setOnCLickListener(block: () -> Unit) {
        this.addGestureRecognizer(
            UITapGestureRecognizer(
                Target(block),
                NSSelectorFromString("invokeBlock")
            )
        )
    }

    private fun tapAction() {
        println("webView tapped")
        notifyAdClicked()
    }
}
