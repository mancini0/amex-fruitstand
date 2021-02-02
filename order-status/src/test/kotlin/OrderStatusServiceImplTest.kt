package com.amex.fruitstand.orderstatus

import com.amex.fruitstand.proto.OrderStatusOuterClass.StreamOrderEventsRequest
import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.Ignore

class OrderStatusServiceImplTest {
    val mockConsumer = MockConsumer<String, ByteArray>(OffsetResetStrategy.EARLIEST)
    val subscribers = mutableMapOf<String, MutableStateFlow<Order>>()
    val orderStatusService = OrderStatusServiceImpl(mockConsumer, subscribers)

    @Test
    @Ignore
    fun kafkaOrderEventsShouldEmitToGrpcSubsribers() {
        runBlocking {
            val job = orderStatusService.beginOrderEventKafkaSubscription()
            val flow = orderStatusService.streamOrderEvents(StreamOrderEventsRequest.newBuilder()
                    .setUserId("mike").build())

            val order = Order.newBuilder().setOrderId("oid")
                    .setUserId("mike")
                    .setAppleQty(3).setOrangeQty(1).setCost(175).build()
            assertThat(subscribers.containsKey("mike")).isTrue()
            mockConsumer.addRecord(ConsumerRecord("order-status", 0, 0L, "mike", order.toByteArray()))
            assertThat(flow.single()).isEqualTo(order)
            job.cancel()
        }
    }

}