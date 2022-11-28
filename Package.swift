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
            checksum: "724193d402e95da97cbf23803b3d6be346470bb36189c715519f45a4ec7973c1"
        ),
    ]
)
