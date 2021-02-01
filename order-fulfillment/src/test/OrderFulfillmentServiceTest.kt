package com.amex.fruitstand.fulfillment

import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import com.google.common.truth.Truth.assertThat
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer

class OrderFulfillmentServiceTest {

    @Test
    fun fulfillOrderWhenSufficientInventoryExists() {
        val mockOrderConsumer = MockConsumer<String, ByteArray>(OffsetResetStrategy.EARLIEST)
        val mockOrderStatusProducer = MockProducer<String, ByteArray>(true, StringSerializer(), ByteArraySerializer())
        val orderFulfillmentService = OrderFulfillmentService(mockOrderConsumer, mockOrderStatusProducer,
                orangeInventory = AtomicInteger(2), appleInventory = AtomicInteger(1))
        val order = Order.newBuilder().setOrderId("o123")
                .setOrderStatus(Order.Status.OPEN)
                .setUserId("mike")
                .setAppleQty(3)
                .setOrangeQty(3)
                .build()

        val exepectedOrderStatus = order.toBuilder().setOrderStatus(Order.Status.FILLED).build()
        orderFulfillmentService.fulfillOrder(order)
        assertThat(mockOrderStatusProducer.history()
                .contains(ProducerRecord("order-status", order.userId, exepectedOrderStatus.toByteArray())))
    }
    
    @Test
    fun rejectOrderWhenOutOfStock() {
        val mockOrderConsumer = MockConsumer<String, ByteArray>(OffsetResetStrategy.EARLIEST)
        val mockOrderStatusProducer = MockProducer<String, ByteArray>(true, StringSerializer(), ByteArraySerializer())
        val orderFulfillmentService = OrderFulfillmentService(mockOrderConsumer, mockOrderStatusProducer,
                orangeInventory = AtomicInteger(2), appleInventory = AtomicInteger(1))
        val order = Order.newBuilder().setOrderId("o123")
                .setOrderStatus(Order.Status.OPEN)
                .setUserId("mike")
                .setAppleQty(1)
                .setOrangeQty(3)
                /**too many oranges to fill!**/
                .build()

        val exepectedOrderStatus = order.toBuilder().setOrderStatus(Order.Status.REJECTED_OUT_OF_STOCK).build()
        orderFulfillmentService.fulfillOrder(order)
        assertThat(mockOrderStatusProducer.history()
                .contains(ProducerRecord("order-status", order.userId, exepectedOrderStatus.toByteArray())))


    }

}