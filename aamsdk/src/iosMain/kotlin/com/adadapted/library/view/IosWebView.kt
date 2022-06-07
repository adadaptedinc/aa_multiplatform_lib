package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.interfaces.WebViewListener
import com.adadapted.library.nsDataUTF8
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import platform.WebKit.*

class IosWebView : WKWebView (frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration()),
        WKUIDelegateProtocol,
        WKNavigationDelegateProtocol,
        UIGestureRecognizerDelegateProtocol,
        UIScrollViewDelegateProtocol {

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
            .setContentInsetAdjustmentBehavior(UIScrollViewContentInsetAdjustmentBehavior
                .UIScrollViewContentInsetAdjustmentNever)

        val tap = UITapGestureRecognizer(
            target = this,
            action = NSSelectorFromString("adTapped")
        )

        tap.numberOfTapsRequired = 1u
        tap.delegate = this
        this.addGestureRecognizer(tap)
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
    }

    fun loadBlank() {
        currentAd = Ad()
        val dummyDocument =
            "<html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head><body></body></html>"
        val data = dummyDocument.nsDataUTF8()
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

    // UIScrollView Delegate
    override fun viewForZoomingInScrollView(scrollView: UIScrollView): UIView? {
        return null
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

    // Gesture Recognizer Delegate
    @ObjCAction
    fun adTapped() {
        notifyAdClicked()
    }

    override fun webView(webView: WKWebView, didFailProvisionalNavigation: WKNavigation?, withError: NSError) {
        //forced override
    }

    override fun gestureRecognizer(
        gestureRecognizer: UIGestureRecognizer,
        shouldRecognizeSimultaneouslyWithGestureRecognizer: UIGestureRecognizer
    ): Boolean {
        return true
    }

    // NOOP WebView Overrides
    override fun webView(
        webView: WKWebView,
        contextMenuWillPresentForElement: WKContextMenuElementInfo
    ) {
        // NOOP
    }
}
