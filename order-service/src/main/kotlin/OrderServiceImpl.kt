package com.amex.fruitstand.order

import com.amex.fruitstand.discount.DiscountService
import com.amex.fruitstand.price.PriceService
import com.amex.fruitstand.proto.OrderStatusOuterClass.Order
import com.amex.fruitstand.proto.OrderStatusOuterClass.Order.*
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ExecutionException

class OrderServiceImpl(
        private val priceService: PriceService,
        private val discountService: DiscountService,
        private val orderProducer: Producer<String, ByteArray>) : OrderService {
    companion object {
        val log = LoggerFactory.getLogger(OrderServiceImpl::class.java)
        const val INVALID_ORDER_ERROR_MSG = "Invalid order format. The order input string must be of the form o:a, \n" +
                "where both o and a are positive integers less than or equal to 100,\n" +
                "with o representing the number of oranges you wish to purchase, and a representing the \n" +
                "number of apples you wish to purchase."

        const val FULFILLMENT_UNREACHABLE = "Sorry, we cancelled your order because we could not reach our fulfillment center."
    }

    override fun processOrder(orderInput: String): OrderSubmissionResult {
        if (orderInput.count { c -> c == ':' } != 1) {
            return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG)
        }
        try {
            val orderBuilder = Order.newBuilder()
                    .setUserId("happy-customer")
                    .setOrderId(UUID.randomUUID().toString())
            var orderSubmissionResult = orderInput.split(":")
                    .map { qty -> Integer.parseInt(qty) }
                    .filter { qty -> qty >= 0 && qty <= 100 }

                    .zipWithNext()
                    .ifEmpty { return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG) }
                    .onEach { (oranges, apples) ->
                        orderBuilder
                                .setOrangeQty(oranges)
                                .setAppleQty(apples)
                    }
                    .map { (oranges, apples) -> discountService.applyDiscount(oranges, apples) }
                    .map { discountResult ->
                        OrderSubmissionResult(((discountResult.effectiveOrangeQty * priceService.getOrangeUnitPrice()) +
                                (discountResult.effectiveAppleQty * priceService.getAppleUnitPrice())),
                                null)
                    }
                    .onEach { it.orderPrice?.let { cost -> orderBuilder.setCost(cost) } }
                    .single()
            val order = orderBuilder.build()
            orderProducer.send(ProducerRecord("orders", order.orderId, order.toByteArray())).get()

            return orderSubmissionResult

        } catch (e: NumberFormatException) {
            return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG)
        } catch (e: ExecutionException) {
            return OrderSubmissionResult(orderPrice = null, errorMessage = FULFILLMENT_UNREACHABLE)
        }


    }


}