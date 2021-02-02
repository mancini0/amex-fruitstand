package com.amex.fruitstand.orderstatus

import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import io.grpc.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

fun main() {
    val subscriberMap: MutableMap<String, MutableStateFlow<Order>> = ConcurrentHashMap()
    val consumerProps = Properties()
    val bootstrapServers = System.getenv("bootstrap.servers") ?: "localhost:9092";
    consumerProps.put("bootstrap.servers", bootstrapServers)
    consumerProps.put("group.id", "status-listeners")
    consumerProps.put("enable.auto.commit", "true")
    consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

    val orderEventConsumer: KafkaConsumer<String, ByteArray> = KafkaConsumer(consumerProps)
    val orderStatusService = OrderStatusServiceImpl(orderEventConsumer, subscriberMap)
    orderStatusService.beginOrderEventKafkaSubscription()
    println("starting grpc server...")
    ServerBuilder
            .forPort(9309)
            .addService(orderStatusService)
            .build()
            .start()
            .awaitTermination()
}