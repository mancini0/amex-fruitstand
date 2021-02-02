package com.amex.fruitstand.orderstatus

import com.amex.fruitstand.proto.OrderStatusGrpcKt.OrderStatusCoroutineImplBase
import com.amex.fruitstand.proto.OrderStatusOuterClass.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.Consumer

import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import java.time.Duration


class OrderStatusServiceImpl(private val kafkaConsumer: Consumer<String, ByteArray>,
                             private val subscribers: MutableMap<String, MutableStateFlow<Order>>)
    : OrderStatusCoroutineImplBase() {

    companion object {
        private val log = LoggerFactory.getLogger(OrderStatusServiceImpl::class.java)
    }

    fun beginOrderEventKafkaSubscription(): Job {
        val job = GlobalScope.launch {
            log.debug("beginning subscription to order-events topic")
            kafkaConsumer.assign(listOf(TopicPartition("order-status", 0)))
            while (true) {
                for (message in kafkaConsumer.poll(Duration.ofMillis(1000))) {
                    log.info("read message {} from order-status", message)
                    val userId = message.key()
                    val order = Order.parseFrom(message.value())
                    subscribers[userId]?.emit(order)
                }
            }
        }
        return job
    }

    override fun streamOrderEvents(request: StreamOrderEventsRequest): Flow<Order> {
        val flow = MutableStateFlow<Order>(Order.getDefaultInstance())
        flow.filterNot { it.equals(Order.getDefaultInstance()) }
        log.info("starting order event subscription for userId={}...", request.userId)
        subscribers[request.userId] = flow
        return flow
    }

}