load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")

kt_jvm_library(
    name = "order-fulfillment-lib",
    srcs = [
        "src/main/kotlin/OrderFulfillmentService.kt"
    ],
    deps = [
        "//proto:kt_grpc_order_status",
        "//proto:order_status_java_proto",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

kt_jvm_binary(
    name = "order-fulfillment",
    srcs = [
        "src/main/kotlin/App.kt"
    ],
    main_class = "com.amex.fruitstand.fulfillment.AppKt",
    deps = [
          ":order-fulfillment-lib",
          "@maven//:ch_qos_logback_logback_classic",
    ],
)

kt_jvm_test(
    name = "order-fulfillment-test",
    srcs = [
        "src/test/OrderFulfillmentServiceTest.kt"
    ],
    test_class = "com.amex.fruitstand.fulfillment.OrderFulfillmentServiceTest",
    deps = [
        ":order-fulfillment-lib",
        "@maven//:junit_junit",
        "@maven//:com_google_truth_truth",
        "@maven//:ch_qos_logback_logback_classic",
    ],
)