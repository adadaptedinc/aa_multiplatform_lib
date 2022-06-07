package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.constraintsToFillSuperview
import com.adadapted.library.helpers.Base64.base64decoded
import com.adadapted.library.nsDataUTF8
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.*
import platform.UIKit.*
import platform.WebKit.*

enum class LinkType {
    POP_UP,
    EXTERNAL
}

@ExportObjCClass
class LinkOrPopup : UIViewController,
    WKUIDelegateProtocol,
    WKNavigationDelegateProtocol,
    UIGestureRecognizerDelegateProtocol {

    private val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    private var webView =
        WKWebView(frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration())
    private var doneButton = UIButton.buttonWithType(UIButtonTypeClose)

    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    constructor(ad: Ad, linkType: LinkType) : super(nibName = null, bundle = null) {
        view.translatesAutoresizingMaskIntoConstraints = false
        setupWebView()
        setupDoneButton()

        val urlString = iosEndpoint(ad.actionPath)
        val url = NSURL(string = urlString)
        val request = NSURLRequest(uRL = url)
        webView.loadRequest(request)

        when (linkType) {
            LinkType.POP_UP -> presentPopup()
            LinkType.EXTERNAL -> presentExternalLink()
        }
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(webView)
        view.addSubview(doneButton)
        setupConstraints()
        webView.setNeedsDisplay()
    }

    private fun presentPopup() {
        viewController?.presentModalViewController(this, animated = true)
    }

    private fun presentExternalLink() {
        viewController?.presentViewController(this, animated = true, completion = null)
    }

    private fun detailedItemTitleFrom(string: String) {
        val detailedItemTitle = itemFromDecodedData(string.base64decoded.nsDataUTF8())
        val itemAsDictionary: Map<Any?, *>? = mapOf("detailedItem" to detailedItemTitle)
        NSNotificationCenter.defaultCenter.postNotificationName(
            "addDetailedListItem",
            null,
            itemAsDictionary
        )
    }

    private fun itemFromDecodedData(itemData: NSData?): String? {
        val detailedItemsJson =
            convertToDictionary(itemData)?.valueForKey("detailed_list_items") as NSArray
        val detailedItem = detailedItemsJson.firstObject as NSDictionary

        return detailedItem.valueForKey("product_title") as String?
    }

    private fun convertToDictionary(data: NSData?): NSDictionary? {
        val json = try {
            if (data != null) {
                NSJSONSerialization.JSONObjectWithData(data, NSJSONWritingPrettyPrinted, null)
            } else {
                return null
            }
        } catch (error: Throwable) {
            println(LOG_TAG + "Error parsing JSON: $error")
        }
        return json as NSDictionary
    }

    private fun iosEndpoint(string: String): String {
        return string.replace("android", "ios", true)
    }

    @ObjCAction
    fun doneButtonTapped() {
        this.dismissViewControllerAnimated(true, completion = null)
    }

    private fun setupConstraints() {
        val constraints = mutableListOf<NSLayoutConstraint>()
        constraints += view.constraintsToFillSuperview()
        constraints += webView.constraintsToFillSuperview()
        constraints.add(
            doneButton.topAnchor.constraintEqualToAnchor(
                view.topAnchor,
                constant = 10.0
            )
        )
        constraints.add(
            doneButton.leftAnchor.constraintEqualToAnchor(
                view.leftAnchor,
                constant = 10.0
            )
        )
        NSLayoutConstraint.activateConstraints(constraints)
    }

    private fun setupWebView() {
        webView.setUIDelegate(this)
        webView.setNavigationDelegate(this)
        webView.userInteractionEnabled = true
        webView.translatesAutoresizingMaskIntoConstraints = false
        webView.setAllowsBackForwardNavigationGestures(false)
        webView.scrollView().bounces = false
        webView.scrollView().setContentInsetAdjustmentBehavior(
            UIScrollViewContentInsetAdjustmentBehavior.UIScrollViewContentInsetAdjustmentNever
        )
        webView.navigationDelegate = this
        webView.UIDelegate = this
    }

    private fun setupDoneButton() {
        doneButton.translatesAutoresizingMaskIntoConstraints = false
        doneButton.tintColor = UIColor.blackColor
        doneButton.backgroundColor = UIColor.clearColor
        doneButton.addTarget(
            target = this,
            action = NSSelectorFromString("doneButtonTapped"),
            forControlEvents = UIControlEventTouchUpInside
        )
    }

    override fun webView(webView: WKWebView, contextMenuWillPresentForElement: WKContextMenuElementInfo) {
        //forced override
    }

    override fun webView(webView: WKWebView, didCommitNavigation: WKNavigation?) {
        //forced override
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
            val rawString = decidePolicyForNavigationAction.request.URL?.absoluteString
            if (rawString?.startsWith("content:") == true) {
                detailedItemTitleFrom(rawString.substring(8))
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            }
        }
    }

    override fun webView(
        webView: WKWebView,
        didFailNavigation: WKNavigation?,
        withError: NSError
    ) {
        println(LOG_TAG + withError.localizedDescription)
    }
}
