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
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

kt_jvm_binary(
    name = "order-status-grpc-server",
    srcs = [
        "src/main/kotlin/App.kt",
    ],
    main_class = "com.amex.fruitstand.orderstatus.AppKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        ":order-status-lib",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:io_grpc_grpc_netty",
    ],
)

kt_jvm_binary(
    name = "order-status-console-grpc-client",
    srcs = [
        "src/main/kotlin/ConsoleOrderStatusListener.kt",
    ],
    main_class = "com.amex.fruitstand.orderstatus.ConsoleOrderStatusListenerKt",
    resources = ["src/main/resources/logback.xml"],
    deps = [
        "//proto:kt_grpc_order_status",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:io_grpc_grpc_netty",
    ],
)

kt_jvm_test(
    name = "order-status-test",
    srcs = [
        "src/test/kotlin/OrderStatusServiceImplTest.kt",
    ],
    test_class = "com.amex.fruitstand.orderstatus.OrderStatusServiceImplTest",
    deps = [
        ":order-status-lib",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
