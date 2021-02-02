package com.amex.fruitstand.orderstatus

import com.amex.fruitstand.proto.OrderStatusGrpcKt
import com.amex.fruitstand.proto.OrderStatusOuterClass.*
import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    val channel = ManagedChannelBuilder.forAddress("localhost", 9309).usePlaintext().build()
    val stub = OrderStatusGrpcKt.OrderStatusCoroutineStub(channel)
    stub.streamOrderEvents(StreamOrderEventsRequest.newBuilder()
            .setUserId("happy-customer").build()).collect { o -> println(o) }

}