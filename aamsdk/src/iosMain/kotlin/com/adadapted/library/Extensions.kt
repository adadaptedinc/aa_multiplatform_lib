package com.adadapted.library

import platform.Foundation.*
import platform.UIKit.*

fun UIView.constraintsToFillSuperview(): List<NSLayoutConstraint> {
    val horizontal = constraintsToFillSuperviewHorizontally()
    val vertical = constraintsToFillSuperviewVertically()
    return vertical + horizontal
}

private fun UIView.constraintsToFillSuperviewVertically(): List<NSLayoutConstraint> {
    val superview = superview ?: return emptyList()
    val top = topAnchor.constraintEqualToAnchor(superview.topAnchor)
    val bottom = bottomAnchor.constraintEqualToAnchor(superview.bottomAnchor)
    return listOf(top, bottom)
}

private fun UIView.constraintsToFillSuperviewHorizontally(): List<NSLayoutConstraint> {
    val superview = superview ?: return emptyList()
    val leader = leadingAnchor.constraintEqualToAnchor(superview.leadingAnchor)
    val trailer = trailingAnchor.constraintEqualToAnchor(superview.trailingAnchor)
    return listOf(leader, trailer)
}

fun String.nsDataUTF8(): NSData? =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)
