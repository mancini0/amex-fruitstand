package com.amex.fruitstand.price;

interface PriceService {
    /**returns the cost of an apple in US cents.**/
    fun getAppleUnitPrice() : Int

    /**returns the cost of an orange in US cents **/
    fun getOrangeUnitPrice() : Int
}