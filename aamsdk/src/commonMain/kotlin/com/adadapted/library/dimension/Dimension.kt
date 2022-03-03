package com.adadapted.library.dimension

class Dimension(var height: Int = 0, var width: Int = 0) {
    object Orientation {
        const val LAND = "land"
        const val PORT = "port"
    }
}