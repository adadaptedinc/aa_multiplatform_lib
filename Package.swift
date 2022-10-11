// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "aa_multiplatform_lib",
    platforms: [
        .iOS(.v14)
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
            checksum: "d968726ccc7a3122f7d68ab8d895cb314cc12b8913412cf7d1ea160834f1cd12"
        ),
    ]
)
