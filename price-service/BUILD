package(default_visibility = ["//order-service:__pkg__"])
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")


kt_jvm_library(
    name = "price-service",
    srcs = [
        "src/main/kotlin/PriceService.kt",
    ],
    deps = [],
)

kt_jvm_library(
    name = "price-service-impl",
    srcs = [
        "src/main/kotlin/PriceServiceImpl.kt"
    ],
    deps = [":price-service"],
)