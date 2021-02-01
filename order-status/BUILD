load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_binary", "kt_jvm_library", "kt_jvm_test")

kt_jvm_library(
    name = "order-status-lib",
    srcs = [
        "src/main/kotlin/OrderStatusServiceImpl.kt",
    ],
    deps = [
        "//proto:kt_grpc_order_status",
        "//proto:order_status_java_proto",
        "@io_grpc_grpc_java//context",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)

kt_jvm_binary(
    name = "order-status",
    srcs = [
        "src/main/kotlin/App.kt"
    ],
    main_class = "com.amex.fruitstand.orderstatus.AppKt",
    deps = [
          ":order-status-lib",
          "@maven//:ch_qos_logback_logback_classic",
    ],
)

kt_jvm_test(
    name = "order-status-test",
    srcs = [
        "src/test/kotlin/OrderStatusServiceImplTest.kt"
    ],
    test_class = "com.amex.fruitstand.orderstatus.OrderStatusServiceImplTest",
    deps = [
        ":order-status-lib",
        "@maven//:junit_junit",
        "@maven//:com_google_truth_truth",
        "@maven//:ch_qos_logback_logback_classic",
    ],
)