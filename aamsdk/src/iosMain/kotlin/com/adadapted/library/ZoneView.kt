package com.adadapted.library

import aa_multiplatform_lib.cinterop.UIViewWithOverridesProtocol
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import platform.WebKit.*

class ZoneView(): UIView (frame = cValue { CGRectZero }), UIViewWithOverridesProtocol {

    private lateinit var adView: WKWebView

    init {
        adView = WKWebView(frame = cValue { CGRectZero }, configuration = WKWebViewConfiguration()).apply {
            translatesAutoresizingMaskIntoConstraints = false

        }
        addSubview(adView)
        translatesAutoresizingMaskIntoConstraints = false
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
    }

    private fun setupConstraints() {
        val constraints = mutableListOf<NSLayoutConstraint>()
        constraints.add(leadingAnchor.constraintEqualToAnchor(leadingAnchor))
        constraints.add(topAnchor.constraintEqualToAnchor(topAnchor))
        constraints.add(bottomAnchor.constraintEqualToAnchor(topAnchor, 120.0))
        constraints.add(centerXAnchor.constraintEqualToAnchor(centerXAnchor))
        constraints.add(centerYAnchor.constraintEqualToAnchor(centerYAnchor))

        constraints += adView.constraintsToFillSuperview()

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
