package(default_visibility = ["//visibility:public"])

load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_grpc_library")

proto_library(
    name = "order_status_proto",
    srcs = ["order_status.proto"],
    deps = [],
)

java_proto_library(
    name = "order_status_java_proto",
    deps = [":order_status_proto"],
)

kt_jvm_grpc_library(
    name = "kt_grpc_order_status",
    srcs = [":order_status_proto"],
    deps = [":order_status_java_proto"],
)

proto_library(
    name = "cashier_proto",
    srcs = ["cashier.proto"],
    deps = [],
)

java_proto_library(
    name = "cashier_java_proto",
    deps = [":cashier_proto"],
)

kt_jvm_grpc_library(
    name = "kt_grpc_cashier",
    srcs = [":cashier_proto"],
    deps = [":cashier_java_proto"],
)
