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
            url: "https://github.com/adadaptedinc/aa_multiplatform_lib/aa_multiplatform_lib.zip",
            checksum: "da5a2a501abf8d20431197667b99f16c2be330cac03171b7af8fd9fcb2b1aa44"
        ),
    ]
)
