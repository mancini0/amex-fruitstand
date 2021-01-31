load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_test")

kt_jvm_library(
    name = "order-service",
    srcs = [
        "src/main/kotlin/OrderService.kt",
        "src/main/kotlin/OrderServiceImpl.kt",
    ],
    deps = [
        "//discount-service",
        "//price-service",
    ],
)

kt_jvm_binary(
    name = "order-entry-cli",
    srcs = ["src/main/kotlin/OrderEntryCli.kt"],
    main_class = "com.amex.fruitstand.order.OrderEntryCliKt",
    deps = [
        ":order-service",
        "//price-service:price-service-impl",
    ],
)

kt_jvm_test(
    name = "order-service-test",
    srcs = ["src/test/kotlin/OrderServiceImplTest.kt"],
    test_class = "com.amex.fruitstand.order.OrderServiceImplTest",
    deps = [
        ":order-service",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
