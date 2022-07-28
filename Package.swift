// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "aa_multiplatform_lib",
    platforms: [
        .iOS(.v14.1)
    ],
    products: [
        .library(
            name: "aa_multiplatform_lib",
            targets: ["aa_multiplatform_lib"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "aa_multiplatform_lib",
            url: "https://gitlab.com/adadapted/aa_multiplatform_lib/aa_multiplatform_lib.zip",
            checksum: "e184da90d1333ded2a76c50ce6979a44876942a244f384abab9e5a65dcb40ff6"
        ),
    ]
)
