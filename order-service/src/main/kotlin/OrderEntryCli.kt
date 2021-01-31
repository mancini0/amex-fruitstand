package com.amex.fruitstand.order

import com.amex.fruitstand.price.PriceServiceImpl

fun main() {
    val priceService = PriceServiceImpl()
    val orderService = OrderServiceImpl(priceService)
    println("Welcome to the fruit stand! Please enter an order in a format like 3:1. \n" +
            "The number to the left of the colon is the number of apples you wish to purchase, \n" +
            "the number to the right of the colon is the number of oranges you wish to purchase. \n" +
            "example: an order of 1:3 means the customer would like to purchase 1 apple and 3 oranges. \n" +
            "example: an order of 2:0 means the customer would like to purchase 2 apples and no oranges. \n" +
            "To exit the fruitstand, type q or Q.");

    println("\nThe current orange price is ${priceService.getOrangeUnitPrice()} cents.")
    println("\nThe current apple price is ${priceService.getAppleUnitPrice()} cents.")

    while (true) {
        val order = readLine() ?: ""
        if (order.toUpperCase().equals("Q")) break
        val orderSubmissionResult = orderService.processOrder(order)
        orderSubmissionResult.orderPrice?.let { println("Thanks! Your total is $it cents.") }
        orderSubmissionResult.errorMessage?.let { println(it) }
        println("ready to serve the next customer, please enter your order below, or q to quit.")
    }
    println("goodbye!")

}
