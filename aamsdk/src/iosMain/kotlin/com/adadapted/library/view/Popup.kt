package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdContent
import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.atl.AddToListItem
import com.adadapted.library.atl.PopupContent
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.constraintsToFillSuperview
import com.adadapted.library.event.EventClient
import com.adadapted.library.helpers.Base64.base64decoded
import com.adadapted.library.log.AALogger
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
class Popup : UIViewController,
    WKUIDelegateProtocol,
    WKNavigationDelegateProtocol,
    UIGestureRecognizerDelegateProtocol {

    private val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    private var webView = WKWebView(frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration())
    private var ad: Ad? = null
    private var doneButton = UIButton.buttonWithType(UIButtonTypeClose)

    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    constructor(ad: Ad, linkType: LinkType) : super(nibName = null, bundle = null) {
        view.translatesAutoresizingMaskIntoConstraints = false
        setupWebView()
        setupDoneButton()

        this.ad = ad

        val urlString = iosEndpoint(ad.actionPath)

        if (urlString.startsWith("http")) {
            val url = NSURL(string = urlString)
            val request = NSURLRequest(uRL = url)
            webView.loadRequest(request)
        } else {
            EventClient.trackSdkError(
                EventStrings.POPUP_URL_MALFORMED,
                "Incorrect Action Path URL supplied for Ad: " + ad.id
            )
        }

        deliverAdContent(ad)

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

    private fun deliverAdContent(ad: Ad) {
        val params = HashMap<String, String>()
        params["ad_id"] = ad.id
        EventClient.trackSdkEvent(EventStrings.POPUP_CONTENT_CLICKED, params)
        val content = AdContent.createAddToListContent(ad)
        AddItContentPublisher.publishAdContent(content)
    }

    private fun createAddToListItem(itemData: String) {
        val payloadId = ad?.payload?.payloadId

        val jsonDictionary =
            convertToDictionary(itemData.base64decoded.nsDataUTF8())?.valueForKey("detailed_list_items") as NSArray
        val detailedItem = jsonDictionary.firstObject as NSDictionary

        val trackingId = detailedItem.valueForKey("tracking_id") as String
        val title = detailedItem.valueForKey("product_title") as String
        val brand = detailedItem.valueForKey("product_brand") as String
        val category = detailedItem.valueForKey("product_category") as String
        val barCode = detailedItem.valueForKey("product_barcode") as String
        val retailerSku = detailedItem.valueForKey("product_discount") as String
        val productImage = detailedItem.valueForKey("product_image") as String

        val items: MutableList<AddToListItem> = ArrayList()
        items.add(
            AddToListItem(
                trackingId,
                title,
                brand,
                category,
                barCode,
                retailerSku,
                productImage
            )
        )

        val content = payloadId?.let { PopupContent.createPopupContent(it, items) }
        if (content != null) {
            AddItContentPublisher.publishPopupContent(content)
        }
    }

    private fun trackDetailedItem(string: String) {
        val trackingId = trackingIdFromDecodedData(string.base64decoded.nsDataUTF8())

        val params = HashMap<String, String>()
        params["tracking_id"] = trackingId
        EventClient.trackSdkEvent(EventStrings.POPUP_ATL_CLICKED, params)
    }

    private fun trackingIdFromDecodedData(itemData: NSData?): String {
        val detailedItemsJson = convertToDictionary(itemData)?.valueForKey("detailed_list_items") as NSArray
        val detailedItem = detailedItemsJson.firstObject as NSDictionary

        return detailedItem.valueForKey("tracking_id") as String
    }

    private fun convertToDictionary(data: NSData?): NSDictionary? {
        val json = try {
            if (data != null) {
                NSJSONSerialization.JSONObjectWithData(data, NSJSONWritingPrettyPrinted, null)
            } else {
                return null
            }
        } catch (error: Throwable) {
            AALogger.logError("Error parsing JSON: $error")
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
            val rawString = decidePolicyForNavigationAction.request.URL?.absoluteString as String
            if (rawString.startsWith("content:")) {
                val data = rawString.removePrefix("content:")
                createAddToListItem(data)
                trackDetailedItem(data)
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            }
        }
    }

    override fun webView(
        webView: WKWebView,
        didFailNavigation: WKNavigation?,
        withError: NSError
    ) {
        AALogger.logError(withError.localizedDescription)
    }
}
