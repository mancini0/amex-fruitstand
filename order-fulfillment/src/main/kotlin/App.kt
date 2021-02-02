package com.amex.fruitstand.fulfillment

import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

fun main() = runBlocking {
    val consumerProps = Properties()
    val producerProps = Properties()
    val bootstrapServers = System.getenv("bootstrap.servers") ?: "localhost:9092";

    consumerProps.put("bootstrap.servers", bootstrapServers)
    consumerProps.put("group.id", "fulfillment")
    consumerProps.put("enable.auto.commit", "true")
    consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

    producerProps.put("bootstrap.servers", bootstrapServers);
    producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    producerProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    producerProps.put("acks", "0")
    producerProps.put("linger.ms", "500")

    val orderConsumer: KafkaConsumer<String, ByteArray> = KafkaConsumer(consumerProps)
    val orderStatusProducer: KafkaProducer<String, ByteArray> = KafkaProducer(producerProps)
    val fulfillmentService = OrderFulfillmentService(orderConsumer, orderStatusProducer,
            AtomicInteger(20), AtomicInteger(20))

    val orderSubscription = fulfillmentService.subscribeToOrderEvents()
    
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            orderSubscription.cancel()
        }
    })

    orderSubscription.join()

}