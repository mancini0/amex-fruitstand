package com.amex.fruitstand.order

import java.math.BigInteger

data class OrderSubmissionResult(val orderPrice: Int?, val errorMessage: String?)

interface OrderService {
    fun processOrder(orderInput: String): OrderSubmissionResult
}