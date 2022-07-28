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
            checksum: "a2a0e13623beb888f53d974e566c4eb157129872a72d8f2c0929a591c5f70bb1"
        ),
    ]
)
