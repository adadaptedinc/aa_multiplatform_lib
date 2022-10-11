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
            checksum: "4c95fbba8fae844937e93037be50db100ed3f9acbe19c2981c8b2cef7198ef31"
        ),
    ]
)
