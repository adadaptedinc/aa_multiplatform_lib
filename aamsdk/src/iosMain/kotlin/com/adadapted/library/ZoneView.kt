package com.adadapted.library

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.UIKit.*
import platform.WebKit.*

actual class ZoneView(): WKWebView (frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration()), UIViewWithOverridesProtocol {

    init {
        this.translatesAutoresizingMaskIntoConstraints = false
        this.setAllowsBackForwardNavigationGestures(false)
        this.scrollView().bounces = false
        this.scrollView().setContentInsetAdjustmentBehavior(UIScrollViewContentInsetAdjustmentBehavior.UIScrollViewContentInsetAdjustmentNever)
        setupConstraints()
    }

    override fun layoutSubviews() {
        println("layoutSubviews")
        setNeedsDisplay()
    }

    override fun drawRect(aRect: CValue<CGRect>) {
        val rectAsString = aRect.useContents {
            "" + this.origin.x + ", " + this.origin.y + ", " + (this.origin.x +this.size.width) + ", " + (this.origin.y +this.size.height)
        }
        println("ZoneView dimensions: $rectAsString")
        loadAdView()
    }

    fun loadAdView() {
        val link = "https://sandbox.adadapted.com/a/NWY0NTZIODZHNWY0;101942;45445"
        val request = NSURLRequest(NSURL(string = link))
        print(link)
        loadRequest(request)
    }

    private fun setupConstraints() {
        val constraints = mutableListOf<NSLayoutConstraint>()
        constraints += this.constraintsToFillSuperview()
        NSLayoutConstraint.activateConstraints(constraints)
    }

    private fun UIView.constraintsToFillSuperview(): List<NSLayoutConstraint> {
        val horizontal = constraintsToFillSuperviewHorizontally()
        val vertical = constraintsToFillSuperviewVertically()
        return vertical + horizontal
    }

    private fun constraintsToFillSuperviewVertically(): List<NSLayoutConstraint> {
        val superview = superview ?: return emptyList()
        val top = topAnchor.constraintEqualToAnchor(superview.topAnchor)
        val bottom = bottomAnchor.constraintEqualToAnchor(superview.bottomAnchor)
        return listOf(top, bottom)
    }

    private fun constraintsToFillSuperviewHorizontally(): List<NSLayoutConstraint> {
        val superview = superview ?: return emptyList()
        val leader = leadingAnchor.constraintEqualToAnchor(superview.leadingAnchor)
        val trailer = trailingAnchor.constraintEqualToAnchor(superview.trailingAnchor)
        return listOf(leader, trailer)
    }
}

fun createZoneView(): UIView = ZoneView()

//val webView = WKWebView.alloc()
//webView.init()
//
//webView.setUIDelegate(object : WKUIDelegate {})// if you want more control over the UIDelegate, just override methods
//webView.setNavigationDelegate(object : WKNavigationDelegate {})// if you want more control over the NavigationDelegate, just override methods
//setView(webView)
//val urlString = "https://threejs.org/examples/#webgl_loader_mmd"
//// val urlString = "https://threejs.org/examples/#webgl_buffergeometry_instancing_billboards"
//// val urlString = "https://threejs.org/examples/#webgl_lines_fat_wireframe"
//val url = NSURL.URLWithString(urlString)
//val nsurlRequest = NSURLRequest.requestWithURL(url)
//webView.loadRequest(nsurlRequest)
//webView.setAllowsBackForwardNavigationGestures(true)
//webView.scrollView().setBounces(false)
//webView.scrollView().setContentInsetAdjustmentBehavior(UIScrollViewContentInsetAdjustmentBehavior.Never) //remove safearea !!!