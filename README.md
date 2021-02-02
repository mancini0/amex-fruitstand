###Usage:

I use the [Bazel](https://bazel.build/) build system. (If you have Bazel installed,
replace the `bazelisk` commands below with `bazel`.)

#####to run the order-entry command line application:
`./bazelisk-linux-amd64 run --javabase=@bazel_tools//tools/jdk:remote_jdk11  --java_runtime_version=remotejdk_11 //order-service:order-entry-cli` (linux)


`BAZEL_VERSION=4.0.0 ./bazelisk-darwin-amd64 run --java_runtime_version=remotejdk_11 --javabase=@bazel_tools//tools/jdk:remote_jdk11  //order-service:order-entry-cli` (mac)

