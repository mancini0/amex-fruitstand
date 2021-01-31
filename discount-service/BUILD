package(default_visibility = ["//order-service:__pkg__"])

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_test")

kt_jvm_library(
    name = "discount-service",
    srcs = [
        "src/main/kotlin/DiscountService.kt",
        "src/main/kotlin/DiscountServiceImpl.kt",
    ],
    deps = [],
)

kt_jvm_test(
    name = "discount-service-test",
    srcs = [
        "src/test/kotlin/DiscountServiceImplTest.kt",
    ],
    test_class = "com.amex.fruitstand.discount.DiscountServiceImplTest",
    deps = [
        ":discount-service",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
