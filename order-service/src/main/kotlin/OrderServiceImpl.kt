package com.amex.fruitstand.order

import com.amex.fruitstand.price.PriceService

class OrderServiceImpl(private val priceService: PriceService) : OrderService {
    companion object {
        const val INVALID_ORDER_ERROR_MSG = "Invalid order format. The order input string must be of the form o:a, \n" +
                "where both a and b are positive integers less than or equal to ${Integer.MAX_VALUE},\n" +
                "with o representing the number of oranges you wish to purchase, and a representing the \n" +
                "number of apples you wish to purchase."
    }

    override fun processOrder(orderInput: String): OrderSubmissionResult {
        if (orderInput.count { c -> c == ':' } != 1) {
            return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG)
        }
        return try {
            orderInput.split(":")
                    .map { qty -> Integer.parseInt(qty) }
                    .filter { qty -> qty >= 0 }
                    .zipWithNext()
                    .ifEmpty { return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG) }
                    .map { (oranges, apples) ->
                        OrderSubmissionResult(((oranges * priceService.getOrangeUnitPrice()) +
                                (apples * priceService.getAppleUnitPrice())).toBigInteger(), null)
                    }
                    .single()
        } catch (e: NumberFormatException) {
            return OrderSubmissionResult(orderPrice = null, errorMessage = INVALID_ORDER_ERROR_MSG)
        }

    }


}