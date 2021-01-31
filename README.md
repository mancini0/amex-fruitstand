###Usage:

I use the [Bazel](https://bazel.build/) build system. (If you have Bazel installed,
replace the `bazelisk` commands below with `bazel`.)

#####to run the order-entry command line application:

`./bazelisk-linux-amd64 run //order-service:order-entry-cli` (linux)


`./bazelisk-darwin-amd64 run //order-service:order-entry-cli` (mac)


`./bazelisk-windows-amd64.exe run //order-service:order-entry-cli` (windows)


#####to run all tests

`./bazelisk-linux-amd64 test //...` (linux)


`./bazelisk-darwin-amd64 test //...` (mac)


`./bazelisk-windows-amd64.exe test //...` (windows)