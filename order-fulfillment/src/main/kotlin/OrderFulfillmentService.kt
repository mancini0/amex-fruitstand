package com.amex.fruitstand.fulfillment

import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger


/** A real implementation would have exception handling surrounding the kafka connectivity,
 * exception handling surrounding the deserialization of kafka messages' byte arrays into Order objects,
 * and validation of inbound orders (verifying that the order has a userId, valid stock, etc.), and
 * but kafka messages would be read and produced in a transactional manner.
 * I did not add such functionality  for this assignment.
 */

open class OrderFulfillmentService(private val orderConsumer: Consumer<String, ByteArray>,
                                   private val orderStatusProducer: Producer<String, ByteArray>,
                                   private val orangeInventory: AtomicInteger,
                                   private val appleInventory: AtomicInteger
) {

    companion object {
        private val log = LoggerFactory.getLogger(OrderFulfillmentService::class.java)
    }

    fun subscribeToOrderEvents(): Job {
        val job = GlobalScope.launch {
            log.info("subscribing to orders topic...")
            orderConsumer.subscribe(listOf("orders"))
            while (true) {
                for (orderMessage in orderConsumer.poll(Duration.ofMillis(3500))) {
                    log.info("received order message with key= {}", orderMessage.key())
                    fulfillOrder(Order.parseFrom(orderMessage.value()))
                }
            }
        }
        return job
    }

    fun fulfillOrder(order: Order) {
        val orderStatusBuilder = order.toBuilder()
        if (appleInventory.addAndGet(order.appleQty * -1) < 0) {
            //put the stock back, (i.e do not partially fill order)
            appleInventory.addAndGet(order.appleQty)
            orderStatusBuilder.setOrderStatus(Order.Status.REJECTED_OUT_OF_STOCK)
        } else if (orangeInventory.addAndGet(order.orangeQty * -1) < 0) {
            orangeInventory.addAndGet(order.orangeQty)
            orderStatusBuilder.setOrderStatus(Order.Status.REJECTED_OUT_OF_STOCK)
        } else {
            //items are in stock
            orderStatusBuilder.setOrderStatus(Order.Status.FILLED)
            log.info("after fulfillment, our remaining orange inventory: {}," +
                    "remaining apple inventory: {}", orangeInventory.get(), appleInventory.get())
        }
        val payload = orderStatusBuilder.build().toByteArray()
        orderStatusProducer.send(ProducerRecord<String, ByteArray>("order-status", order.userId, payload),
                Callback({ recordMetadata, exception ->
                    exception?.let { log.warn("error sending order status", it) }
                            ?: log.info("send to order-status topic was succesful (offset: ${recordMetadata.offset()})")
                }))

    }


}