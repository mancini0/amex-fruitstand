###Usage:

I use the [Bazel](https://bazel.build/) build system. (If you have Bazel installed,
replace the `bazelisk` commands below with `bazel`.)

![Alt text](screenshot1.png?raw=true "order-status-service")
![Alt text](screenshot2.png?raw=true "order-entry-cli")
![Alt text](screenshot3.png?raw=true "fulfillment-service")
![Alt text](screenshot4.png?raw=true "order-status-grpc-listener")

####to run the various services:


`confluent local start kafka`

`kafka-topics --create --topic orders --replication-factor=1 --partitions=1 --bootstrap-server=localhost:9092`

`kafka-topics --create --topic order-status --replication-factor=1 --partitions=1 --bootstrap-server=localhost:9092`

`bazel run //order-service:order-entry-cli`

`bazel run //order-fulfillment   (in a seperate terminal)`

`bazel run //order-status:order-status-grpc-server (in a seperate terminal)`

`bazel run //order-status:order-status-console-grpc-client (in a seperate terminal)'`


######Note, if you do not have Bazel or a JRE installed, you can use the bazelisk binary commited to this repo:

##### on mac, replace bazel with :
`./bazelisk-darwin-amd64 run --java_runtime_version=remotejdk_11 --javabase=@bazel_tools//tools/jdk:remote_jdk11
`

##### on linux, replace bazel with:
`bazelisk-linux-amd64 run --javabase=@bazel_tools//tools/jdk:remote_jdk11 --java_runtime_version=remotejdk_11`