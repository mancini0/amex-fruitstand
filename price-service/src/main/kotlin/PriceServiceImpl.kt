package com.amex.fruitstand.price

class PriceServiceImpl : PriceService {

    override fun getOrangeUnitPrice(): Int {
        return 60;
    }

    override fun getAppleUnitPrice(): Int {
        return 100;
    }

}